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

import java.time.LocalDateTime;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ptGroup")
public class PtGroupController {
    private final SiteUserService siteUserService;
    private final PtGroupService ptGroupService;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;


    @GetMapping("/join")
    public String joinUser(@AuthenticationPrincipal UserDetail userDetail){
        boolean isTrainer = userDetail.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_TRAINER"));
        if(!isTrainer)
            return "redirect:/";

        return "ptGroup/ptgroupjoin";
    }

    @PostMapping("/join")
    public String joinUser(@AuthenticationPrincipal UserDetail userDetail, @RequestParam("number")String number, Model model){
        boolean isTrainer = userDetail.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_TRAINER"));
        if(!isTrainer)
            return "redirect:/";

        SiteUser siteUser = siteUserService.getUser(userDetail.getUsername());
        SiteUser member = siteUserService.getUser(number);

        PtGroup ptGroup1 = ptGroupService.ifJoin(siteUser,member);
        if(ptGroup1 == null){
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

            chatMessageService.firstChatMessage(siteUser,chatRoom);

            siteUserService.save(member);
            return "redirect:/";
        }else{

            model.addAttribute("error","이미 등록된 회원입니다.");
            return "ptGroup/ptgroupjoin";
        }
    }
}
