package com.uit.se104.feedback_system.controller;

import com.uit.se104.feedback_system.entity.Reply;
import com.uit.se104.feedback_system.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/replies")
public class ReplyController {
    @Autowired
    private ReplyService replyService;

    @PostMapping
    public ResponseEntity<Reply> createReply(@RequestBody Reply reply) {
        return ResponseEntity.ok(replyService.saveReply(reply));
    }
}
