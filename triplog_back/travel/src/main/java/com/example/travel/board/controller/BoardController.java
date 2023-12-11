package com.example.travel.board.controller;

import com.example.travel.board.dto.BoardListDto;
import com.example.travel.board.entity.Board;
import com.example.travel.board.service.BoardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.Charset;
import java.util.Map;

@CrossOrigin(origins = { "*" }, methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.POST} , maxAge = 6000)
@Slf4j
@RestController
@RequestMapping("/board")
public class BoardController {

    private BoardService boardService;

    public BoardController(BoardService boardService) {
        super();
        this.boardService = boardService;
    }

    @PostMapping
    public ResponseEntity<?> writeArticle(@RequestBody Board boardDto) {
        log.info("writeArticle boardDto - {}", boardDto);

        try {
            boardService.writeArticle(boardDto);
            return new ResponseEntity<Void>(HttpStatus.CREATED);
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }

    @GetMapping
    public ResponseEntity<?> listArticle(@RequestParam Map<String, String> map) {
        log.info("listArticle map - {}", map);

        try {
            BoardListDto boardListDto = boardService.listArticle(map);
            HttpHeaders header = new HttpHeaders();
            header.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));

            return ResponseEntity.ok().headers(header).body(boardListDto);
        } catch (Exception e) {
            return exceptionHandling(e);
        }
    }

    @GetMapping("/{articleno}")
    public ResponseEntity<Board> getArticle(@PathVariable("articleno") Long articleno) throws Exception {
        log.info("getArticle - 호출 : " + articleno);

        boardService.updateHit(articleno);
        return new ResponseEntity<Board>(boardService.getArticle(articleno).get(), HttpStatus.OK);
    }

    @GetMapping("/modify/{articleno}")
    public ResponseEntity<Board> getModifyArticle(@PathVariable("articleno") Long articleno) throws Exception {
        log.info("getModifyArticle - 호출 : " + articleno);

        return new ResponseEntity<>(boardService.getArticle(articleno).get(), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<String> modifyArticle(@RequestBody Board boardDto) throws Exception {
        log.info("modifyArticle - 호출 {}", boardDto);

        boardService.modifyArticle(boardDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{articleno}")
    public ResponseEntity<String> deleteArticle(@PathVariable("articleno") Long articleno) throws Exception {
        log.info("deleteArticle - 호출");
        boardService.deleteArticle(articleno);
        return ResponseEntity.ok().build();

    }

    private ResponseEntity<String> exceptionHandling(Exception e) {
        log.info(e.getMessage());

        return new ResponseEntity<>("Error : " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}