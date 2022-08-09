package kr.wegather.wegather.controller;

import io.swagger.annotations.ApiOperation;
import kr.wegather.wegather.service.AwsS3Service;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/s3")
public class AmazonS3Controller {
    private final AwsS3Service awsS3Service;

    @ApiOperation(value = "S3에 이미지 업로드")
    @PostMapping("/image")
    public ResponseEntity<String> uploadImage(@RequestPart MultipartFile image) {
        String filename = awsS3Service.uploadImage(image);

        JSONObject res = new JSONObject();
        try {
            res.put("filename", filename);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(res.toString());
    }

    @ApiOperation(value = "S3에서 이미지 삭제")
    @DeleteMapping("/image")
    public ResponseEntity deleteImage(@RequestBody deleteImageRequest request) {
        String imageURL = request.imageURL;
        awsS3Service.deleteImage(imageURL);

        return new ResponseEntity(HttpStatus.OK);
    }


    @Data
    static class deleteImageRequest {
        String imageURL;
    }
}
