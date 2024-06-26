package com.example.MMP.ptGroup;

import com.example.MMP.chat.ChatMessage;
import com.example.MMP.chat.ChatMessageService;
import com.example.MMP.chat.ChatRoom;
import com.example.MMP.chat.ChatRoomService;
import com.example.MMP.security.UserDetail;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ptGroup")
public class PtGroupController {
    private final SiteUserService siteUserService;
    private final PtGroupService ptGroupService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @PostMapping("/join")
    public String joinUser(@AuthenticationPrincipal UserDetail userDetail, @RequestParam("number") String number, RedirectAttributes redirectAttributes) {
        boolean isTrainer = userDetail.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_TRAINER"));

        if (!isTrainer) {
            redirectAttributes.addFlashAttribute("error", "트레이너가 아닙니다.");
            return "redirect:/user/userList";
        }

        if (number == null || number.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "회원번호를 입력해주세요.");
            return "redirect:/user/userList";
        }

        SiteUser siteUser;
        SiteUser member;
        try {
            siteUser = siteUserService.getUser(userDetail.getUsername());
            member = siteUserService.getUser(number);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "유효하지 않은 회원번호입니다.");
            return "redirect:/user/userList";
        }

        if (member == null) {
            redirectAttributes.addFlashAttribute("error", "존재하지 않는 회원번호입니다.");
            return "redirect:/user/userList";
        }

        PtGroup ptGroup1 = ptGroupService.ifJoin(siteUser, member);
        if (ptGroup1 == null) {
            PtGroup ptGroup = ptGroupService.findByTrainer(siteUser);
            ptGroup.getMembers().add(member);
            ptGroupService.save(ptGroup);
            member.getPtGroupUser().add(ptGroup);

            ChatRoom chatRoom = new ChatRoom();
            chatRoom.setSort("one");
            chatRoomService.save(chatRoom);
            chatRoom.getUserList().add(siteUser);
            chatRoom.getUserList().add(member);

            siteUser.getChatRoomList().add(chatRoom);
            member.getChatRoomList().add(chatRoom);

            chatMessageService.firstChatMessage(siteUser, chatRoom);

            siteUserService.save(member);
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("error", "이미 등록된 회원입니다.");
            return "redirect:/user/userList";
        }
    }
}
