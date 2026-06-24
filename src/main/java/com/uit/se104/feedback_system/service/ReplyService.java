package com.uit.se104.feedback_system.service;

import com.uit.se104.feedback_system.entity.Reply;
import com.uit.se104.feedback_system.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReplyService {
    @Autowired
    private ReplyRepository replyRepository;

    public Reply saveReply(Reply reply) {
        return replyRepository.save(reply);
    }
}
