package src.quiz.service;

import com.tinify.Options;
import com.tinify.Source;
import com.tinify.Tinify;
import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@Service
public class MinioService {
    @Autowired
    MinioClient minioClient;
    @Value("${minio.buckek.name}")
    String defaultBucketName;
    @Autowired
    ImgPostService imgPostService;

    @Value("${tinify.key}")
    String tinifyKey;

    @Value("${minio.default.folder}")
    String defaultBaseFolder;

    public List<Bucket> getAllBuckets() {
        try {
            return minioClient.listBuckets();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public Iterable<Result<Item>> getAllObject() {
        try {
            return minioClient.listObjects(ListObjectsArgs.builder().bucket("tung").build());
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public void uploadFile(String name, Path tempFile) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        UploadObjectArgs.Builder builder = UploadObjectArgs.builder().bucket(defaultBucketName)
                .object(name).filename(tempFile.toString());
        minioClient.uploadObject(builder.build());
    }

    public void removeFile(String nameObject) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClient.removeObject(RemoveObjectArgs.builder().bucket(defaultBucketName).object(nameObject).build());
    }

    public void uploadAndTinypng(MultipartFile files[], int idPost) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        Tinify.setKey(tinifyKey);
        for (int i = 0; i < files.length; i++) {
            String img_name = files[i].getOriginalFilename();
            String[] parts = img_name.split("\\.(?=[^\\.]+$)");

            Path tempFile = Files.createTempFile(parts[0], "." + parts[1]);
            try (InputStream in = files[i].getInputStream()) {
                Files.copy(in, tempFile, StandardCopyOption.REPLACE_EXISTING);
            }
            Source source = Tinify.fromFile(tempFile.toString());
            source.toFile(tempFile.toString());

            Options options = new Options().with("method", "scale").with("width", 1024);
            Source resized = source.resize(options);
            resized.toFile(tempFile.toString());

            String name_img = "post_" + idPost + "_" + i + "." + parts[1];
            this.uploadFile(name_img, tempFile);

        }
    }
}
