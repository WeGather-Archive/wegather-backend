package kr.wegather.wegather.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AwsS3Service {
    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.url}")
    private String url;

    public String uploadImage(MultipartFile file) {
        String filename = createFileName(file.getOriginalFilename());
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(file.getSize());
        objectMetadata.setContentType(file.getContentType());

        try (InputStream inputStream = file.getInputStream()) {
            amazonS3.putObject(new PutObjectRequest(bucket, filename, inputStream, objectMetadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다.");
        }

        return url.concat(filename);
    }

    public void deleteImage(String URL) {
        String filename = URL.substring(URL.lastIndexOf("/") + 1);
        System.out.println(filename);
        amazonS3.deleteObject(new DeleteObjectRequest(bucket, filename));
    }

    private String createFileName(String filename) {
        return UUID.randomUUID().toString().concat(getFileExtension(filename));
    }

    private String getFileExtension(String filename) {
        try {
            String ext = filename.substring(filename.lastIndexOf(".")).toLowerCase();
            if (
                ext.equals(".jpg") ||
                ext.equals(".jpeg") ||
                ext.equals(".jpe") ||
                ext.equals(".png") ||
                ext.equals(".gif")
            )
                return ext;
            else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일입니다. (" + filename + ")");
            }
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "잘못된 형식의 파일입니다. (" + filename + ")");
        }
    }
}
