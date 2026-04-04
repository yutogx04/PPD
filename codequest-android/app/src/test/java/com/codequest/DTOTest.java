package com.codequest;
import com.codequest.model.dto.LoginRequest;
import com.codequest.model.dto.RegisterRequest;
import com.codequest.model.dto.SubmitCodeRequest;
import com.codequest.model.dto.VerifyOTPRequest;
import com.codequest.model.dto.ForgotPasswordRequest;
import org.junit.Test;
import static org.junit.Assert.*;
public class DTOTest {
    @Test
    public void loginRequest_setsEmailAndPassword() {
        LoginRequest req = new LoginRequest("test@email.com", "password123");
        assertEquals("test@email.com", req.getEmail());
        assertEquals("password123", req.getPassword());
    }
    @Test
    public void loginRequest_emptyFields() {
        LoginRequest req = new LoginRequest("", "");
        assertEquals("", req.getEmail());
        assertEquals("", req.getPassword());
    }
    @Test
    public void registerRequest_setsAllFields() {
        RegisterRequest req = new RegisterRequest("CodeWizard",
                "test@email.com", "password123", "BEGINNER");
        assertEquals("test@email.com", req.getEmail());
        assertEquals("password123", req.getPassword());
        assertEquals("CodeWizard", req.getPseudo());
        assertEquals("BEGINNER", req.getLevel());
    }
    @Test
    public void registerRequest_differentLevels() {
        RegisterRequest beginner = new RegisterRequest("u1", "a@b.c", "pw", "BEGINNER");
        RegisterRequest inter = new RegisterRequest("u1", "a@b.c", "pw", "INTERMEDIATE");
        RegisterRequest advanced = new RegisterRequest("u1", "a@b.c", "pw", "ADVANCED");
        assertEquals("BEGINNER", beginner.getLevel());
        assertEquals("INTERMEDIATE", inter.getLevel());
        assertEquals("ADVANCED", advanced.getLevel());
    }
    @Test
    public void submitCodeRequest_setsCodeAndLanguage() {
        SubmitCodeRequest req = new SubmitCodeRequest(
                "def hello():\n    print('Hello')", "PYTHON");
        assertEquals("def hello():\n    print('Hello')", req.getCode());
        assertEquals("PYTHON", req.getLanguage());
    }
    @Test
    public void submitCodeRequest_emptyCode() {
        SubmitCodeRequest req = new SubmitCodeRequest("", "PYTHON");
        assertEquals("", req.getCode());
    }
    @Test
    public void verifyOTPRequest_setsEmailAndCode() {
        VerifyOTPRequest req = new VerifyOTPRequest("test@email.com", "123456");
        assertEquals("test@email.com", req.getEmail());
        assertEquals("123456", req.getOtpCode());
    }
    @Test
    public void verifyOTPRequest_6digitCode() {
        VerifyOTPRequest req = new VerifyOTPRequest("a@b.c", "000000");
        assertEquals(6, req.getOtpCode().length());
    }
    @Test
    public void forgotPasswordRequest_setsEmail() {
        ForgotPasswordRequest req = new ForgotPasswordRequest("test@email.com");
        assertEquals("test@email.com", req.getEmail());
    }
}
