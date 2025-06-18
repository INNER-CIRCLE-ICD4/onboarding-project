package icd.onboarding.surveyproject.survey.controller;

import icd.onboarding.surveyproject.survey.common.controller.ApiResponse;
import icd.onboarding.surveyproject.survey.controller.consts.ErrorCodes;
import icd.onboarding.surveyproject.survey.controller.dto.CreateSurveyRequest;
import icd.onboarding.surveyproject.survey.controller.dto.CreateSurveyResponse;
import icd.onboarding.surveyproject.survey.controller.dto.SurveyResponse;
import icd.onboarding.surveyproject.survey.controller.exception.CommonSurveyHttpException;
import icd.onboarding.surveyproject.survey.service.SurveyService;
import icd.onboarding.surveyproject.survey.service.domain.Survey;
import icd.onboarding.surveyproject.survey.service.exception.SurveyNotFoundException;
import jakarta.validation.Valid;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
	ApiResponse<SurveyResponse> getSurvey (
			@PathVariable(name = "surveyId") @NotNull UUID surveyId,
			@PathVariable(name = "version") int version
	) {
		try {
			Survey response = surveyService.findByIdAndVersion(surveyId, version);

			return ApiResponse.just(SurveyResponse.formDomain(response));
		} catch (SurveyNotFoundException e) {
			throw new CommonSurveyHttpException(
					ErrorCodes.SURVEY_NOT_FOUND,
					HttpStatus.NOT_FOUND
			);
		}
	}

	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping
	ApiResponse<CreateSurveyResponse> createSurvey (@RequestBody @Valid CreateSurveyRequest request) {

		return ApiResponse.just(CreateSurveyResponse.fromDomain(null));
	}
}
