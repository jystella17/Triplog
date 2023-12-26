package com.example.travel.route.service;

import com.example.travel.route.dto.request.RouteCreateDto;
import com.example.travel.route.dto.response.RouteResponseDto;
import com.example.travel.route.entity.Image;
import com.example.travel.route.entity.Route;
import com.example.travel.route.repository.ImageRepository;
import com.example.travel.route.repository.RouteRepository;
import com.example.travel.route.S3.S3Uploader;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final S3Uploader uploader;
    private final RouteRepository routeRepository;
    private final ImageRepository imageRepository;

    @Transactional
    public List<RouteResponseDto> findAllRoute(){
        List<Route> route = routeRepository.findAll();
        List<RouteResponseDto> routeList = new ArrayList<RouteResponseDto>();

        for(Route r: route) {
            RouteResponseDto info = RouteResponseDto.builder().routeId(r.getId()).cityName(r.getCity().getCityName())
                            .keyword(r.getKeyword().getKeyword()).routeName(r.getRouteName()).routeDay(r.getRouteDay())
                            .imgUrl(r.getThumbnail().getImgUrl()).build();
        }

        return routeList;
    }

    @Transactional
    public RouteResponseDto findRouteById(Long route_id) {
        Route route = routeRepository.findById(route_id).get();

        return RouteResponseDto.builder().routeId(route.getId()).cityName(route.getCity().getCityName())
                .keyword(route.getKeyword().getKeyword()).routeName(route.getRouteName()).routeDay(route.getRouteDay())
                .imgUrl(route.getThumbnail().getImgUrl()).build();
    }

    // 검색 단어를 포함하는 루트
    @Transactional
    public List<RouteResponseDto> findRouteByName(String route_name) {
        List<Route> routeList = routeRepository.findByRouteName(route_name);
        List<RouteResponseDto> result = new ArrayList<RouteResponseDto>();

        for(Route r: routeList) {
            RouteResponseDto info = RouteResponseDto.builder().routeId(r.getId()).cityName(r.getCity().getCityName())
                    .keyword(r.getKeyword().getKeyword()).routeName(r.getRouteName()).routeDay(r.getRouteDay())
                    .imgUrl(r.getThumbnail().getImgUrl()).build();
        }

        return result;
    }

    @Transactional
    public List<RouteResponseDto> findRouteByKeyName(String keyword){
        return routeRepository.findRouteByKeyName(keyword);
    }

    @Transactional
    public List<RouteResponseDto> findRouteByKeyNameTop3(String keyword){
        return routeRepository.findByKeywordOrderById(keyword);
    }

    @Transactional
    public List<RouteResponseDto> findRouteByCityName(String cityName){
        return routeRepository.findRouteByCityName(cityName);
    }

    @Transactional
    public List<RouteResponseDto> findRouteByKeyword(Long key_id){
        return routeRepository.findRouteByKeyword(key_id);
    }

    @Transactional
    public List<RouteResponseDto> findRouteByUser(Long uid){
        return routeRepository.findRouteByUser(uid);
    }

    @Transactional
    public List<RouteResponseDto> findAllRouteByUser(Long uid) {
        return routeRepository.findAllRouteByUser(uid);
    }

    @Transactional
    public List<RouteResponseDto> findRouteBySpot(String spot_name){
        return routeRepository.findRouteBySpot(spot_name);
    }

    @Transactional
    public Long addRoute(RouteCreateDto route) throws IOException {
        MultipartFile img = route.getImage();
        String imgUrl = uploader.uploadFiles(img, "route");
        String imgName = img.getOriginalFilename();
        String imgKey = img.getContentType();
        Image image = Image.builder().img_name(imgName).img_key(imgKey).img_url(imgUrl).build();
        imageRepository.saveImage(image);
        Route newRoute = Route.builder().uid(route.getUid()).city_id(route.getCity_id())
                        .key_id(route.getKey_id()).route_name(route.getRoute_name())
                        .route_day(route.getRoute_day()).build();

        // Route Image 테이블에도 저장하는 코드 추가
        routeRepository.addRoute(newRoute);
        Long route_id = newRoute.getRoute_id();
//        System.out.println("route_id >> "+route_id+" img_id >> "+img_id);
        RouteImage newRouteImage = RouteImage.builder().route_id(route_id).img_id(image.getImg_id()).img_url(imgUrl).build();
        System.out.println(newRouteImage);
        routeImageRepository.saveRouteImage(newRouteImage);
        return route_id;
    }

    @Transactional
    public int delete(Long route_id){
        return routeRepository.delete(route_id);
    }

    @Transactional
    public int modify(Route route){
        return routeRepository.modify(route);
    }
}