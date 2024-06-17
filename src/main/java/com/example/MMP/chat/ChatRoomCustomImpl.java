package com.example.MMP.chat;

import com.example.MMP.siteuser.SiteUser;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@RequiredArgsConstructor
public class ChatRoomCustomImpl implements ChatRoomCustom{

    private final JPAQueryFactory jpaQueryFactory;

    QChatRoom qChatRoom = QChatRoom.chatRoom;

    public Optional<ChatRoom> findChatroom(SiteUser trainer, SiteUser user){
        return Optional.ofNullable(jpaQueryFactory.selectFrom(qChatRoom)
                .where(qChatRoom.userList.contains(trainer)
                        .and(qChatRoom.userList.contains(user)))
                .fetchOne());
    }

}
