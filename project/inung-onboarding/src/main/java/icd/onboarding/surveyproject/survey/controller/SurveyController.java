package icd.onboarding.surveyproject.survey.controller;

import icd.onboarding.surveyproject.survey.common.controller.ApiResponse;
import icd.onboarding.surveyproject.survey.controller.consts.ErrorCodes;
import icd.onboarding.surveyproject.survey.controller.dto.request.CreateSurveyRequest;
import icd.onboarding.surveyproject.survey.controller.dto.request.SubmitResponseRequest;
import icd.onboarding.surveyproject.survey.controller.dto.request.UpdateSurveyRequest;
import icd.onboarding.surveyproject.survey.controller.dto.response.*;
import icd.onboarding.surveyproject.survey.controller.exception.CommonSurveyHttpException;
import icd.onboarding.surveyproject.survey.service.SurveyService;
import icd.onboarding.surveyproject.survey.service.domain.Response;
import icd.onboarding.surveyproject.survey.service.domain.Survey;
import icd.onboarding.surveyproject.survey.service.exception.*;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/survey")
public class SurveyController {
	private final SurveyService surveyService;

	SurveyController (SurveyService surveyService) {
		this.surveyService = surveyService;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{surveyId}/version/{version}")
	ApiResponse<SurveyResponse> getSurvey (@PathVariable(name = "surveyId") @NotNull UUID surveyId, @PathVariable(name = "version") int version) {
		try {
			Survey response = surveyService.findByIdAndVersion(surveyId, version);

			return ApiResponse.just(SurveyResponse.fromDomain(response));
		} catch (SurveyNotFoundException e) {
			throw new CommonSurveyHttpException(ErrorCodes.SURVEY_NOT_FOUND, HttpStatus.NOT_FOUND);
		}
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	ApiResponse<CreateSurveyResponse> createSurvey (@RequestBody @Valid CreateSurveyRequest request) {
		try {
			Survey response = surveyService.createSurvey(request.toDomain());

			return ApiResponse.just(CreateSurveyResponse.fromDomain(response));
		} catch (UnsupportedOptionException exception) {
			throw new CommonSurveyHttpException(ErrorCodes.UNSUPPORTED_OPTION, HttpStatus.BAD_REQUEST);
		} catch (InSufficientOptionException exception) {
			throw new CommonSurveyHttpException(ErrorCodes.INSUFFICIENT_OPTION, HttpStatus.BAD_REQUEST);
		}
	}

	@ResponseStatus(HttpStatus.OK)
	@PutMapping
	ApiResponse<UpdateSurveyResponse> updateSurvey (@RequestBody @Valid UpdateSurveyRequest request) {
		try {
			Survey response = surveyService.updateSurvey(request.toDomain());

			return ApiResponse.just(UpdateSurveyResponse.fromDomain(response));
		} catch (SurveyNotFoundException exception) {
			throw new CommonSurveyHttpException(ErrorCodes.SURVEY_NOT_FOUND, HttpStatus.NOT_FOUND);
		} catch (InSufficientOptionException exception) {
			throw new CommonSurveyHttpException(ErrorCodes.INSUFFICIENT_OPTION, HttpStatus.BAD_REQUEST);
		} catch (UnsupportedOptionException exception) {
			throw new CommonSurveyHttpException(ErrorCodes.UNSUPPORTED_OPTION, HttpStatus.BAD_REQUEST);
		}
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/{surveyId}/version/{version}/response")
	ApiResponse<SubmitResponseResponse> submitResponse (@PathVariable(name = "surveyId") @NotNull UUID surveyId, @PathVariable(name = "version") int version, @RequestBody @Valid SubmitResponseRequest request) {
		try {
			Response response = surveyService.submitResponse(request.toDomain(surveyId, version));

			return ApiResponse.just(SubmitResponseResponse.fromDomain(response));
		} catch (SurveyNotFoundException exception) {
			throw new CommonSurveyHttpException(ErrorCodes.SURVEY_NOT_FOUND, HttpStatus.NOT_FOUND);
		} catch (RequiredQuestionNotAnsweredException exception) {
			throw new CommonSurveyHttpException(ErrorCodes.REQUIRED_ANSWERS, HttpStatus.BAD_REQUEST);
		} catch (TooManyAnswersException exception) {
			throw new CommonSurveyHttpException(ErrorCodes.TOO_MANY_ANSWER, HttpStatus.BAD_REQUEST);
		} catch (DuplicateResponseException exception) {
			throw new CommonSurveyHttpException(ErrorCodes.DUPLICATE_RESPONSE, HttpStatus.BAD_REQUEST);
		}
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/{surveyId}/version/{version}/response")
	ApiResponse<?> getResponseBySurveyInfo (
			@PathVariable(name = "surveyId") @NotNull UUID surveyId,
			@PathVariable(name = "version") int version
	) {
		try {
			List<Response> responseList = surveyService.findResponsesBySurvey(surveyId, version);
			List<SurveyResponseResponse> response = responseList.stream()
																.map(SurveyResponseResponse::fromDomain)
																.toList();
			return ApiResponse.just(response);
		} catch (SurveyResponseNotFoundException exception) {
			throw new CommonSurveyHttpException(ErrorCodes.RESPONSE_NOT_FOUNT, HttpStatus.NOT_FOUND);
		}
	}
}
