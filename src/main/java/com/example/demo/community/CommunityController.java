package com.example.demo.community;

import com.example.demo.user.dto.UserDTO;
import com.example.demo.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import java.security.Principal;

@RequestMapping("/community")
@RequiredArgsConstructor
@Controller
@PreAuthorize("isAuthenticated()")
public class CommunityController {
    private final CommunityRepository communityRepository;
    private final UserService userService;

    @GetMapping("/checkcommunity")
    public String checkCommunity(Long registernum, Principal principal){
        Community community = this.communityRepository.findByRegisternum(registernum);//서비스에서 만들고 dto로 받아야함
        UserDTO userDTO = this.userService.getUserDto(principal.getName());
        if(community!=null&& userDTO.getRole().equals("사용자(업주)")){
            return "redirect:/community/{userhash}";//해당 프론트 만들어야함
            //관리자의 페이지 이동
        }else if(userDTO.getRole().equals("사용자(업주)")){
            return "redirect:/community/createcommunity";//해당 프론트 만들어야함
        }/*else{
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근권한이 없습니다.");
        }*/
        return "여기 잘못들어갔다고 예외처리 해줘야하는데 어캐하지"; //알림창 뜨고 몇초뒤 다른 페이지 가게하는것도 만들어야할듯
    };

    @GetMapping("/{userhash}") //나중에 업장 번호로 매장 이름 가줘와서 이걸로 매핑할거임
    public String accessedCommunity(@PathVariable String userhash){
        //공지사항,질문게시판 둘건데 기본적으로 공지사항 리스트 페이지로 이동시킬듯?
      return "communitymainpage"; // 커뮤니티 프론트 만들어야함
    };
    @GetMapping("/createcommunity")
    public String createCommunity(Principal principal){
        UserDTO userDTO = this.userService.getUserDto(principal.getName());
        if(!userDTO.getRole().equals("사용자(업주)")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "접근권한이 없습니다.");
        }
        return "createCommunity";
    }
}
