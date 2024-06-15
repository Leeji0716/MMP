package com.example.MMP.chat;

import com.example.MMP.siteuser.SiteUser;

import java.util.Optional;

public interface ChatRoomCustom {
    Optional<ChatRoom> findChatroom(SiteUser trainer, SiteUser user);
}
