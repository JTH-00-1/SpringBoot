package com.example.demo.notice;

import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;
@RequestMapping("/noticePage")
@RequiredArgsConstructor
@Controller
@PreAuthorize("isAuthenticated()")
public class NoticeController {
    private final NoticeService noticeService;
    private final UserService userService;

    @RequestMapping("/notice")
    //관리자 공지사항 메인 페이지 이동
    public String list(Model model, @RequestParam(value="page", defaultValue="1") int page,
                       @RequestParam(value = "kw", required = false) String kw, Principal principal) {
        Page<NoticeDTO> paging;
        if(kw==null) {
            paging = this.noticeService.getList(page);
        }else {
            paging = this.noticeService.getList(page, kw);
        }
        UserDTO userDto=this.userService.getUserDto(principal.getName()); //관리자 확인(등록 버튼 활성화여부)

        int startPage =  Math.max(page - 4, 1);
        int endPage = Math.min(page+4, paging.getTotalPages());

        model.addAttribute("roleid", userDto.getRole());
        model.addAttribute("paging", paging);
        model.addAttribute("nowPage",page);
        model.addAttribute("startPage",startPage); //페이지 리스트 (현재페이지 -4)
        model.addAttribute("endPage", endPage); //페이지 리스트 (현재페이지+4)
        model.addAttribute("kw", kw); //검색 키워드

        return "list_notice";
    }
    @GetMapping("/notice/{noticehash}")
    public String showPost(@PathVariable("noticehash") String noticehash, Model model){
        NoticeDTO noticeDTO = this.noticeService.getNotice(noticehash);
        model.addAttribute("post",noticeDTO);
        model.addAttribute("select","notice");
        return "post_notice";
    }
    @GetMapping("/notice/create")
    public String create(Model model,CreateForm createForm){
        model.addAttribute("select","notice");
        return "create_form";
    }

    @PostMapping("/notice/create")
    public String create(@Valid CreateForm createForm, Principal principal, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "create_form";
        }
        this.noticeService.create(createForm.getTitle(), createForm.getContent(),principal.getName());
        return "redirect:/noticePage/notice";
    }

    @GetMapping("/notice/modify/{noticehash}")
    public String modifyPost(@PathVariable("noticehash") String noticehash, CreateForm createForm, Principal principal,Model model){
        NoticeDTO noticeDTO = this.noticeService.getNotice(noticehash);
        if(!noticeDTO.getUid().getNickname().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        createForm.setTitle(noticeDTO.getTitle());
        createForm.setContent(noticeDTO.getContent());
        model.addAttribute("select","notice");
        return "create_form";
    }
    @PostMapping("/notice/modify/{noticehash}")
    public String modifyPost(@PathVariable("noticehash") String noticehash, @Valid CreateForm createForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "notice/{noticehash}";
        }
        this.noticeService.modify(noticehash, createForm.getTitle(), createForm.getContent());
        return "redirect:/noticePage/notice/{noticehash}";
    }
    @GetMapping("/notice/delete/{noticehash}")
    public String deletePost(@PathVariable("noticehash") String noticehash, Principal principal){
        if(!noticeService.getNotice(noticehash).getUid().getNickname().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        else{
            this.noticeService.delete(noticehash);
        }
        return "redirect:/noticePage/notice";
    }
}
