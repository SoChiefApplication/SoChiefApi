package fr.vlegall.sochief.controller

import fr.vlegall.sochief.exception.NotFoundException
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.dao.DataIntegrityViolationException
import org.slf4j.LoggerFactory
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice
class ApiExceptionHandler {

    private val log = LoggerFactory.getLogger(ApiExceptionHandler::class.java)

    @ExceptionHandler(NotFoundException::class)
    fun handleNotFound(ex: NotFoundException, req: HttpServletRequest): ProblemDetail {
        val pd = ProblemDetail.forStatus(HttpStatus.NOT_FOUND)
        pd.title = "Resource not found"
        pd.detail = ex.message ?: "Not found"
        pd.setProperty("path", req.requestURI)
        return pd
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException, req: HttpServletRequest): ProblemDetail {
        val pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
        pd.title = "Bad request"
        pd.detail = ex.message ?: "Invalid request"
        pd.setProperty("path", req.requestURI)
        return pd
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    fun handleTypeMismatch(ex: MethodArgumentTypeMismatchException, req: HttpServletRequest): ProblemDetail {
        val pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
        pd.title = "Bad request"
        pd.detail = "Invalid value for parameter '${ex.name}'"
        pd.setProperty("path", req.requestURI)
        return pd
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException, req: HttpServletRequest): ProblemDetail {
        val pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
        pd.title = "Validation error"
        pd.detail = "Request validation failed"
        pd.setProperty("path", req.requestURI)

        val errors = ex.bindingResult.fieldErrors.map {
            mapOf(
                "field" to it.field,
                "message" to (it.defaultMessage ?: "invalid"),
                "rejectedValue" to it.rejectedValue
            )
        }
        pd.setProperty("errors", errors)
        return pd
    }

    @ExceptionHandler(ConstraintViolationException::class)
    fun handleConstraintViolation(ex: ConstraintViolationException, req: HttpServletRequest): ProblemDetail {
        val pd = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST)
        pd.title = "Validation error"
        pd.detail = "Constraint violations"
        pd.setProperty("path", req.requestURI)
        val errors = ex.constraintViolations.map {
            mapOf(
                "property" to it.propertyPath.toString(),
                "message" to it.message,
                "invalidValue" to it.invalidValue
            )
        }
        pd.setProperty("errors", errors)
        return pd
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrity(ex: DataIntegrityViolationException, req: HttpServletRequest): ProblemDetail {
        val pd = ProblemDetail.forStatus(HttpStatus.CONFLICT)
        pd.title = "Data integrity violation"
        pd.detail = ex.rootCause?.message ?: ex.message ?: "Conflict with database constraints"
        pd.setProperty("path", req.requestURI)
        return pd
    }

    @ExceptionHandler(Exception::class)
    fun handleInternalError(ex: Exception, req: HttpServletRequest): ProblemDetail {
        log.error("Unhandled exception at {}: {}", req.requestURI, ex.message, ex)
        val pd = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR)
        pd.title = "Internal server error"
        pd.detail = "An unexpected error occurred"
        pd.setProperty("path", req.requestURI)
        return pd
    }
}
