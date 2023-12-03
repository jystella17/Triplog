package com.example.travel.board.service;

import com.example.travel.board.dto.BoardListDto;
import com.example.travel.board.entity.Board;
import com.example.travel.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BoardService {

    private BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    public void writeArticle(Board board) throws SQLException {
        boardRepository.writeArticle(board);
    }

    public BoardListDto listArticle(Map<String, String> map) throws SQLException {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("word", map.get("word") == null ? "" : map.get("word"));
        int currentPage = Integer.parseInt(map.get("pgno") == null ? "1" : map.get("pgno"));
        int sizePerPage = Integer.parseInt(map.get("spp") == null ? "20" : map.get("spp"));
        int start = currentPage * sizePerPage - sizePerPage;
        param.put("start", start);
        param.put("listsize", sizePerPage);

        String key = map.get("key");
        param.put("key", key == null ? "" : key);
        if ("nickname".equals(key))
            param.put("key", key == null ? "" : "m.nickname");
        List<Board> list = boardRepository.listArticle(param);

        if ("nickname".equals(key))
            param.put("key", key == null ? "" : "nickname");
        int totalArticleCount = boardRepository.getTotalArticleCount(param);
        int totalPageCount = (totalArticleCount - 1) / sizePerPage + 1;

        BoardListDto boardListDto = new BoardListDto();
        boardListDto.setArticles(list);
        boardListDto.setCurrentPage(currentPage);
        boardListDto.setTotalPageCount(totalPageCount);

        return boardListDto;
    }

    public Board getArticle(Long articleNo) throws SQLException{
        return boardRepository.getArticle(articleNo);
    }

    public void updateHit(Long articleNo) throws SQLException{
        boardRepository.updateHit(articleNo);
    }

    public void modifyArticle(Board board) throws SQLException{
        boardRepository.modifyArticle(board);
    }

    public void deleteArticle(Long articleNo) throws SQLException{
        boardRepository.deleteArticle(articleNo);
    }
}