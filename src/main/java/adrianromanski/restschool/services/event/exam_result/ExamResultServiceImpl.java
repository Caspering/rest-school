package adrianromanski.restschool.services.event.exam_result;

import adrianromanski.restschool.domain.base_entity.event.ExamResult;
import adrianromanski.restschool.exceptions.ResourceNotFoundException;
import adrianromanski.restschool.mapper.event.ExamResultMapper;
import adrianromanski.restschool.model.base_entity.event.EventDTO;
import adrianromanski.restschool.model.base_entity.event.ExamResultDTO;
import adrianromanski.restschool.repositories.event.ExamResultRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;



import static java.util.stream.Collectors.*;

@Slf4j
@Service
public class ExamResultServiceImpl implements ExamResultService {

    private final ExamResultRepository examResultRepository;
    private final ExamResultMapper examResultMapper;

    public ExamResultServiceImpl(ExamResultRepository examResultRepository, ExamResultMapper examResultMapper) {
        this.examResultRepository = examResultRepository;
        this.examResultMapper = examResultMapper;
    }


    /**
     * @return All Exam Results
     */
    @Override
    public List<ExamResultDTO> getAllExamResults() {
        return examResultRepository.findAll()
                .stream()
                .map(examResultMapper::examResultToExamResultDTO)
                .collect(toList());
    }


    /**
     * @param id of the Exam Result to be found
     * @return Exam Result with matching id
     * @throws ResourceNotFoundException if not found
     */
    @Override
    public ExamResultDTO getExamResultByID(Long id) {
        return examResultRepository.findById(id)
                .map(examResultMapper::examResultToExamResultDTO)
                .orElseThrow(ResourceNotFoundException::new);
    }


    /**
     * @return All Exam Result with Grade higher than F
     */
    @Override
    public List<ExamResultDTO> getAllPassedExamResults() {
        return examResultRepository.findAll()
                .stream()
                .filter(e -> !e.getGrade().equals("F"))
                .map(examResultMapper::examResultToExamResultDTO)
                .collect(toList());
    }

    /**
     * @return All Exam Result with Grade F
     */
    @Override
    public List<ExamResultDTO> getAllNotPassedExamResults() {
        return examResultRepository.findAll()
                .stream()
                .filter(e -> e.getGrade().equals("F"))
                .map(examResultMapper::examResultToExamResultDTO)
                .collect(toList());
    }

    /**
     * @param subjectName of ExamResult
     * @return all Passed Exams with matching Subject
     */
    @Override
    public List<ExamResultDTO> getAllPassedForSubject(String subjectName) {
        return examResultRepository.findAll()
                .stream()
                .filter(e -> e.getGrade().equals("F"))
                .filter(e -> e.getExam().getSubject().getName().get().equals(subjectName))
                .map(examResultMapper::examResultToExamResultDTO)
                .collect(toList());
    }

    /**
     * @param subjectName of ExamResult
     * @return all Not Passed Exams with matching Subject
     */
    @Override
    public List<ExamResultDTO> getAllNotPassedForSubject(String subjectName) {
        return examResultRepository.findAll()
                .stream()
                .filter(e -> !e.getGrade().equals("F"))
                .filter(e -> e.getExam().getSubject().getName().get().equals(subjectName))
                .map(examResultMapper::examResultToExamResultDTO)
                .collect(toList());
    }


    /**
     * @return Exam Results grouped by -> Grade -> Exam Name
     */
    @Override
    public Map<String, Map<String, List<ExamResultDTO>>> getResultsGroupedByGradeAndName() {
        return examResultRepository.findAll()
                .stream()
                .map(examResultMapper::examResultToExamResultDTO)
                .collect(
                        groupingBy(
                                ExamResultDTO::getGrade,
                                groupingBy(
                                        EventDTO::getName
                                )
                        )
                );
    }

    /**
     * @return Exam Results grouped by -> Date -> Grade
     */
    @Override
    public Map<LocalDate, Map<String, List<ExamResultDTO>>> getResultGroupedByDateAndGrade() {
        return examResultRepository.findAll()
                .stream()
                .filter(examResult -> examResult.getExam().getTeacher() != null)
                .map(examResultMapper::examResultToExamResultDTO)
                .collect(
                        groupingBy(
                                ExamResultDTO::getDate,
                                groupingBy(
                                        ExamResultDTO::getGrade
                            )
                        )
            );

    }


    /**
     * @param examResultDTO body to be saved
     * @return examResultDTO if successfully saved
     */
    @Override
    public ExamResultDTO createExamResult(ExamResultDTO examResultDTO) {
        examResultRepository.save(examResultMapper.examResultDTOToExamResult(examResultDTO));
        log.info("Exam Result with id: " + examResultDTO.getId() + " successfully saved to database");
        return examResultDTO;
    }


    /**
     * @param id of the examResult and checks if it exist in database
     * @param examResultDTO body to update
     * @return updated examResult body
     * @throws ResourceNotFoundException if not found
     */
    @Override
    public ExamResultDTO updateExamResult(Long id, ExamResultDTO examResultDTO) {
        if(examResultRepository.findById(id).isPresent()) {
            ExamResult examResult = examResultMapper.examResultDTOToExamResult(examResultDTO);
            examResult.setId(id);
            examResultRepository.save(examResult);
            log.info("Exam Result with id: " + id + " successfully saved to database");
            return examResultMapper.examResultToExamResultDTO(examResult);
        } else {
            log.debug("Exam Result with id: " + id + " not found");
            throw new ResourceNotFoundException("Exam Result with id: " + id + " not found");
        }

    }


    /**
     * @param id id of the examResult and checks if it exist in database
     * @throws ResourceNotFoundException if not found
     */
    @Override
    public void deleteExamResultByID(Long id) {
        if(examResultRepository.findById(id).isPresent()) {
            examResultRepository.deleteById(id);
            log.info("Exam Result with id: " + id + " successfully deleted");
        } else {
            log.debug("Exam Result with id: " + id + " not found");
            throw new ResourceNotFoundException("Exam Result with id: " + id + " not found");
        }
    }


}
