package service;

import static com.epam.esm.exceptions.Codes.TAG_BAD_REQUEST;
import static com.epam.esm.exceptions.Codes.TAG_NOT_FOUND;
import static com.epam.esm.exceptions.Messages.TAG_ALREADY_EXISTS;
import static com.epam.esm.exceptions.Messages.TAG_ID_NOT_FOUND;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.FOUND;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import com.epam.esm.Dto.Errors.ErrorDTO;
import com.epam.esm.model.Tag;
import com.epam.esm.repository.TagRepository;
import com.epam.esm.service.TagService;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class TagServiceTest {

    public static final String TAG_NAME = "name";
    public static final Long TAG_ID = 1L;

    @InjectMocks
    private TagService tagService;

    @Mock
    TagRepository tagRepository;

    @Mock
    Tag tag;

    @Test
    public void saveTag_savedCorrectly_returnCreated() {

        Mockito.when(tagRepository.save(any(Tag.class))).thenReturn(tag);
        Mockito.when(tagRepository.findByName(TAG_NAME)).thenReturn(Optional.empty());

        ResponseEntity<?> actual = tagService.saveTag(TAG_NAME);
        Mockito.verify(tagRepository).save(any(Tag.class));

        assertEquals(CREATED, actual.getStatusCode());
        assertEquals(tag, actual.getBody());
    }

    @Test
    public void saveTag_tagAlreadyExists_returnBadRequest() {

        Mockito.when(tagRepository.findByName(TAG_NAME)).thenReturn(Optional.ofNullable(tag));

        ResponseEntity<?> actual = tagService.saveTag(TAG_NAME);
        String message = TAG_ALREADY_EXISTS.formatted(tag.getId());

        ResponseEntity<ErrorDTO> responseEntity = ResponseEntity.badRequest().body(new ErrorDTO(message, TAG_BAD_REQUEST));
        Mockito.verify(tagRepository).findByName(Mockito.eq(TAG_NAME));

        assertEquals(responseEntity.getBody(),actual.getBody());
        assertEquals(responseEntity.getStatusCode(), actual.getStatusCode());
    }

    @Test
    public void saveTag_tagNotValid_returnBadRequest() {

        ResponseEntity<?> actual = tagService.saveTag(null);
        ResponseEntity<ErrorDTO> responseEntity = ResponseEntity.badRequest()
                .body(new ErrorDTO("Tag name is required", TAG_BAD_REQUEST));
        assertEquals(responseEntity.getBody(),actual.getBody());
        assertEquals(responseEntity.getStatusCode(), actual.getStatusCode());
    }

    @Test
    public void getTag_tagExists_returnTag(){

        Mockito.when(tagRepository.existsById(TAG_ID)).thenReturn(true);
        Mockito.when(tagRepository.findById(TAG_ID)).thenReturn(tag);

        ResponseEntity<?> retrievedTag = tagService.getTag(TAG_ID);
        ResponseEntity<?> expected = ResponseEntity.status(FOUND).body(tag);

        Mockito.verify(tagRepository).existsById(Mockito.eq(TAG_ID));
        Mockito.verify(tagRepository).findById(Mockito.eq(TAG_ID));

        assertEquals(expected,retrievedTag);
    }

    @Test
    public void getTag_tagNotExist_returnNotFound(){

        Mockito.when(tagRepository.existsById(TAG_ID)).thenReturn(false);

        ResponseEntity<?> retrievedTag = tagService.getTag(TAG_ID);

        String message = TAG_ID_NOT_FOUND.formatted(TAG_ID);

        ErrorDTO errorResponse = new ErrorDTO(message, TAG_NOT_FOUND);
        ResponseEntity.status(NOT_FOUND).body(errorResponse);
        ResponseEntity<?> expected = ResponseEntity.status(NOT_FOUND).body(errorResponse);

        Mockito.verify(tagRepository).existsById(Mockito.eq(TAG_ID));

        assertEquals(expected,retrievedTag);
    }

    @Test
    public void deleteTag_tagExists_returnFound(){

        Mockito.when(tagRepository.existsById(TAG_ID)).thenReturn(true);

        ResponseEntity<?> retrievedTag = tagService.deleteTag(TAG_ID);
        ResponseEntity<?> expected = ResponseEntity.status(FOUND).body(null);

        Mockito.verify(tagRepository).deleteById(Mockito.eq(TAG_ID));

        assertEquals(expected,retrievedTag);
    }

    @Test
    public void deleteTag_tagNotExists_returnNotFound(){

        Mockito.when(tagRepository.existsById(TAG_ID)).thenReturn(false);

        ResponseEntity<?> retrievedTag = tagService.deleteTag(TAG_ID);

        String message = TAG_ID_NOT_FOUND.formatted(TAG_ID);
        ErrorDTO errorResponse = new ErrorDTO(message, TAG_NOT_FOUND);
        ResponseEntity<?> expected = ResponseEntity.status(NOT_FOUND).body(errorResponse);

        Mockito.verify(tagRepository).existsById(Mockito.eq(TAG_ID));

        assertEquals(expected,retrievedTag);
    }
}