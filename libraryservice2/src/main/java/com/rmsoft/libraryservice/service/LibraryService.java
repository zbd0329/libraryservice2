package com.rmsoft.libraryservice.service;

import com.rmsoft.libraryservice.dto.BookDto;
import com.rmsoft.libraryservice.dto.LoginRequestDto;
import com.rmsoft.libraryservice.dto.SignupRequestDto;
import com.rmsoft.libraryservice.entity.Book;
import com.rmsoft.libraryservice.entity.User;
import com.rmsoft.libraryservice.repository.UserRepository;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LibraryService {

    private UserRepository userRepository;

    @Autowired
    public LibraryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> signup(SignupRequestDto signupRequestDto) {

        String username = signupRequestDto.getUsername();
        String password = signupRequestDto.getPassword();

        //가입자 중복체크
        Optional<User> existingUser = userRepository.findByUsername(username);

        if (existingUser.isPresent()) {
            return new ResponseEntity<>("이미 사용 중인 사용자명입니다.", HttpStatus.BAD_REQUEST);
        }


        // 가입자 데이터 베이스에 저장
        User user = new User(username,password);
        userRepository.save(user);

        String responseMessage = "회원가입 성공";
        return new ResponseEntity<>(responseMessage, HttpStatus.OK);
    }

    public ResponseEntity<String> login(LoginRequestDto loginRequestDto, HttpServletResponse response) {
        String username = loginRequestDto.getUsername();
        String password = loginRequestDto.getPassword();

        // 사용자 확인
        User user = userRepository.findByUsername(username).orElse(null);

        if (user == null) {
            return new ResponseEntity<>("등록된 사용자가 없습니다.", HttpStatus.NOT_FOUND);
        }

        // 비밀번호 확인
        if (!user.getPassword().equals(password)) {
            return new ResponseEntity<>("비밀번호가 일치하지 않습니다.", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>("로그인 성공", HttpStatus.OK);
    }

    // 도서 등록 서비스
//    public BookDto addBook(BookDto bookDto, HttpServletRequest httpServletRequest) {
//
////        //저장할 도서 객체 만들기
////        Book book = new Book(bookDto);
////        BookRepository.save(book);
////        return new ResponseEntity<>("도서 저장 성공", HttpStatus.OK);
//
////
//    }
}
