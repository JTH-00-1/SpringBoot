package com.example.demo.event;

import com.example.demo.notice.CreateForm;
import com.example.demo.notice.NoticeDTO;
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
import java.util.List;

@RequestMapping("/eventPage")
@RequiredArgsConstructor
@Controller
@PreAuthorize("isAuthenticated()")
public class EventController {
    private final EventService eventService;
    private final UserService userService;

    @RequestMapping("/nowevent")
    public String nowEvent(Model model, @RequestParam(value="page", defaultValue="1") int page,
                       @RequestParam(value = "kw", required = false) String kw, Principal principal) {
        String statement = "진행중인이벤트";
        // 상태로 구분하여 진행중인 이벤트 조회 및 갱신 때 종료 기간 체크함

        Page<EventDTO> paging;
        if(kw==null) {
            paging= this.eventService.getListbyStatement(page, statement);
        }else {
            paging= this.eventService.getListbyStatement(page, kw, statement);
        }

        UserDTO userDto=this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)

        int startPage =  Math.max(page - 4, 1);
        int endPage = Math.min(page+4, paging.getTotalPages());

        model.addAttribute("roleid", userDto.getRole());
        model.addAttribute("paging", paging);
        model.addAttribute("nowPage",page);
        model.addAttribute("startPage", startPage); //페이지 리스트 (현재페이지 -4)
        model.addAttribute("endPage", endPage); //페이지 리스트 (현재페이지+4)
        model.addAttribute("kw", kw);
        model.addAttribute("statement",statement);
        
        return "list_event";
    }
    @RequestMapping("/closeevent")
    public String closeEventlist(Model model, @RequestParam(value="page", defaultValue="1") int page,
                       @RequestParam(value = "kw", required = false) String kw, Principal principal) {
        String statement = "종료된이벤트";
        Page<EventDTO> paging;
        if(kw==null) {
            paging= this.eventService.getListbyStatement(page, statement);
        }else {
            paging= this.eventService.getListbyStatement(page, kw, statement);
        }
        UserDTO userDto=this.userService.getUserDto(principal.getName());//관리자 확인(등록 버튼 활성화여부)

        int startPage =  Math.max(page - 4, 1);
        int endPage = Math.min(page+4, paging.getTotalPages());

        model.addAttribute("roleid", userDto.getRole());
        model.addAttribute("paging", paging);
        model.addAttribute("nowPage",page);
        model.addAttribute("startPage", startPage); //페이지 리스트 (현재페이지 -4)
        model.addAttribute("endPage", endPage); //페이지 리스트 (현재페이지+4)
        model.addAttribute("kw", kw);
        model.addAttribute("statement",statement);
        return "list_event";
    }
    @GetMapping("/nowevent/create")
    public String create(Model model,CreateForm createForm){
        model.addAttribute("select","event");
        return "create_form";
    }

    @PostMapping("/nowevent/create")
    public String create(@Valid CreateForm createForm, Principal principal, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "create_form";
        }
        this.eventService.create(createForm.getTitle(), createForm.getContent(),principal.getName(), createForm.getEnddate());
        return "redirect:/eventPage/nowevent";
    }
    @GetMapping("/nowevent/{eventhash}")
    public String showNowEvent(@PathVariable("eventhash") String eventhash, Model model){
        EventDTO eventDTO = this.eventService.getEvent(eventhash);
        model.addAttribute("select","event");
        model.addAttribute("post",eventDTO);
        return "post_notice";
    }
    @GetMapping("/nowevent/modify/{eventhash}")
    public String modifyNowEvent(@PathVariable("eventhash") String eventhash, CreateForm createForm, Principal principal,Model model){
        EventDTO eventDTO = this.eventService.getEvent(eventhash);
        if(!eventDTO.getUid().getNickname().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        createForm.setTitle(eventDTO.getTitle());
        createForm.setContent(eventDTO.getContent());
        createForm.setEnddate(eventDTO.getEnddate());
        model.addAttribute("select","event");
        return "create_form";
    }
    @PostMapping("/nowevent/modify/{eventhash}")
    public String modifyNowEvent(@PathVariable("eventhash") String eventhash, @Valid CreateForm createForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "nowevent/{eventhash}";
        }
        this.eventService.modify(eventhash, createForm.getTitle(), createForm.getContent());
        return "redirect:/eventPage/nowevent/{eventhash}";
    }
    @GetMapping("/nowevent/delete/{eventhash}")
    public String deleteNowEvent(@PathVariable("eventhash")String eventhash,Principal principal){
        if(!eventService.getEvent(eventhash).getUid().getNickname().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }else{
            this.eventService.delete(eventhash);
        }
        return "redirect:eventPage/nowevent";
    }
    @GetMapping("/closeevent/{eventhash}")
    public String showCloseEvent(@PathVariable("eventhash") String eventhash, Model model){
        EventDTO eventDTO = this.eventService.getEvent(eventhash);
        model.addAttribute("select","event");
        model.addAttribute("post",eventDTO);
        return "post_notice";
    }
    @GetMapping("/closeevent/modify/{eventhash}")
    public String modifyCloseEvent(@PathVariable("eventhash") String eventhash, CreateForm createForm, Principal principal,Model model){
        EventDTO eventDTO = this.eventService.getEvent(eventhash);
        if(!eventDTO.getUid().getNickname().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }
        createForm.setTitle(eventDTO.getTitle());
        createForm.setContent(eventDTO.getContent());
        createForm.setEnddate(eventDTO.getEnddate());
        model.addAttribute("select","event");
        return "create_form";
    }
    @PostMapping("/closeevent/modify/{eventhash}")
    public String modifyCloseEvent(@PathVariable("eventhash") String eventhash, @Valid CreateForm createForm, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "closeevent/{eventhash}";
        }
        this.eventService.modify(eventhash, createForm.getTitle(), createForm.getContent());
        return "redirect:/eventPage/closeevent/{eventhash}";
    }
    @GetMapping("/closeevent/delete/{eventhash}")
    public String deleteCloseEvent(@PathVariable("eventhash")String eventhash,Principal principal){
        if(!eventService.getEvent(eventhash).getUid().getNickname().equals(principal.getName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
        }else{
            this.eventService.delete(eventhash);
        }
        return "redirect:eventPage/closeevent";
    }
}
