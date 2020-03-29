package adrianromanski.restschool.services;

import adrianromanski.restschool.domain.Exam;
import adrianromanski.restschool.mapper.ExamMapper;
import adrianromanski.restschool.model.ExamDTO;
import adrianromanski.restschool.repositories.ExamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ExamServiceImplTest {

    private final String NAME = "Final Math Exam";
    private final LocalDate DATE = LocalDate.now();
    private final Long POINTS = 100L;
    private final Boolean PASSED = true;
    private final Long ID = 1L;

    ExamService examService;

    @Mock
    ExamRepository examRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        examService = new ExamServiceImpl(ExamMapper.INSTANCE, examRepository);
    }

    ExamDTO initExamDTO() {
        ExamDTO examDTO = new ExamDTO();
        examDTO.setDate(DATE);
        examDTO.setName(NAME);
        examDTO.setPassed(PASSED);
        examDTO.setPoints(POINTS);
        examDTO.setId(ID);
        return examDTO;
    }

    @Test
    void getAllExams() {
        // given
        List<Exam> exams = Arrays.asList(new Exam(), new Exam(), new Exam());

        when(examRepository.findAll()).thenReturn(exams);

        //when
        List<ExamDTO> examsDTOS = examService.getAllExams();

        assertEquals(examsDTOS.size(), 3);

    }

    @Test
    void createNewExam() {
        // given
        ExamDTO examDTO = initExamDTO();

        Exam savedExam = new Exam();
        savedExam.setPoints(examDTO.getPoints());
        savedExam.setPassed(examDTO.getPassed());
        savedExam.setName(examDTO.getName());
        savedExam.setDate(examDTO.getDate());

        when(examRepository.save(any(Exam.class))).thenReturn(savedExam);

        //when
        ExamDTO savedDTO = examService.createNewExam(examDTO);

        assertEquals(savedDTO.getDate(), examDTO.getDate());
        assertEquals(savedDTO.getName(), examDTO.getName());
        assertEquals(savedDTO.getPassed(), examDTO.getPassed());
        assertEquals(savedDTO.getPoints(), examDTO.getPoints());
    }

    @Test
    void updateExam() {
        // given
        ExamDTO examDTO = initExamDTO();

        Exam updatedExam = new Exam();
        updatedExam.setPoints(examDTO.getPoints());
        updatedExam.setPassed(examDTO.getPassed());
        updatedExam.setName("Just a simple exam"); // Updating name
        updatedExam.setDate(examDTO.getDate());
        updatedExam.setId(examDTO.getId());

        when(examRepository.save(any(Exam.class))).thenReturn(updatedExam);

        //when
        ExamDTO updatedDTO = examService.updateExam(ID, examDTO);

        //then
        assertEquals(updatedExam.getName(), "Just a simple exam");
        assertEquals(updatedExam.getId(), ID);
    }

    @Test
    void getExamById() {
        //given
        Exam exam = new Exam();
        exam.setId(ID);
        exam.setName(NAME);

        when(examRepository.findById(anyLong())).thenReturn(Optional.of(exam));

        // when
        ExamDTO foundDTO = examService.getExamById(ID);

        //then
        assertEquals(foundDTO.getId(), ID);
        assertEquals(foundDTO.getName(), NAME);

    }

    @Test
    void deleteExamById() {
        examRepository.deleteById(ID);
        verify(examRepository, times(1)).deleteById(ID);
    }

}