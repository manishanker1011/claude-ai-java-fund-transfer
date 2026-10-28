package com.portfolio.transfer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest @AutoConfigureMockMvc
class TransferApiIntegrationTest {
 @Autowired MockMvc mvc;
 @Test void validTransferAndReplayDoNotDoubleDebit() throws Exception {
  String request="{\"fromAccount\":101,\"toAccount\":202,\"amount\":10.00,\"idempotencyKey\":\"integration-replay\"}";
  mvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON).content(request)).andExpect(status().isOk()).andExpect(jsonPath("$.status").value("SUCCESS"));
  mvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON).content(request)).andExpect(status().isOk());
  mvc.perform(get("/api/accounts/101")).andExpect(status().isOk()).andExpect(jsonPath("$.balance").value(990.0));
 }
 @Test void badRequestRejected() throws Exception {
  mvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON).content("{\"fromAccount\":101,\"toAccount\":202,\"amount\":-1,\"idempotencyKey\":\"bad\"}")).andExpect(status().isBadRequest());
 }
 @Test void missingAccountRejected() throws Exception {
  mvc.perform(post("/api/transfers").contentType(MediaType.APPLICATION_JSON).content("{\"fromAccount\":999,\"toAccount\":202,\"amount\":1,\"idempotencyKey\":\"missing\"}")).andExpect(status().isNotFound());
 }
}
