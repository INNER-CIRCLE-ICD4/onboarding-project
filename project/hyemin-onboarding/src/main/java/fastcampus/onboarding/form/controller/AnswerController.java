package fastcampus.onboarding.form.controller;

import fastcampus.onboarding.common.response.ApiResponse;
import fastcampus.onboarding.form.dto.request.AnswerRequestDto;
import fastcampus.onboarding.form.dto.response.AnswerDto;
import fastcampus.onboarding.form.dto.response.FormCreateResponseDto;
import fastcampus.onboarding.form.service.AnswerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//응답 컨트롤러
@RestController
@RequiredArgsConstructor
@Slf4j
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping("form/answer/{formSeq}")
    public ResponseEntity<ApiResponse<Void>> createdAnswer(@PathVariable Long formSeq, @RequestBody AnswerRequestDto answer) {
        try{
            answerService.createdAnswer(formSeq, answer);
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류"));        }
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(HttpStatus.CREATED, "응답이 성공적으로 입력되었습니다."));
    }

    @GetMapping("form/answer/{formSeq}")
    public ResponseEntity<ApiResponse<AnswerDto>> getAnswer(@PathVariable Long formSeq) {
        try{
            AnswerDto answer = answerService.getAnswer(formSeq);
            return ResponseEntity.ok(ApiResponse.successWithData(HttpStatus.OK, "설문조사 응답을 성공적으로 조회했습니다.", answer));
        }catch (Exception e){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류", null));        }
    }
}
