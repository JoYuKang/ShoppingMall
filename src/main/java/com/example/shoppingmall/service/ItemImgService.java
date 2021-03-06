package com.example.shoppingmall.service;

import com.example.shoppingmall.entity.ItemImg;
import com.example.shoppingmall.repository.ItemImgRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Transactional
public class ItemImgService {

    @Value("${itemImgLocation}")
    private String itemImgLocation;

    private final ItemImgRepository itemImgRepository;

    private final FileService fileService;

    public void saveItemImg(ItemImg itemImg, MultipartFile itemImgFile) throws Exception {
        String oriImgName = itemImgFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if (!StringUtils.isEmpty(oriImgName)) {
            imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            imgUrl = "/images/item/" + imgName;
        }

        //상품 이미지 정보 저장
        itemImg.updateItemImg(oriImgName, imgUrl, imgName);
        itemImgRepository.save(itemImg);
    }

    public void updateItemImg(Long itemImgid, MultipartFile itemImgFile) throws Exception {
        if (!itemImgFile.isEmpty()) {
            ItemImg saveItemImg = itemImgRepository.findById(itemImgid)
                    .orElseThrow(EntityNotFoundException::new);
            //기존 이미지 삭제
            if (!StringUtils.isEmpty(saveItemImg.getImgName())) {
                fileService.deleteFile(itemImgLocation + "/" + saveItemImg.getImgName());
            }
            String oriImgName = itemImgFile.getOriginalFilename();
            String imgName = fileService.uploadFile(itemImgLocation, oriImgName, itemImgFile.getBytes());
            String imgUrl = "/images/item/" + imgName;
            saveItemImg.updateItemImg(oriImgName, imgUrl, imgName);
        }
    }


}
