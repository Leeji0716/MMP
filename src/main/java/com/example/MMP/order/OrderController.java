package com.example.MMP.order;

import com.example.MMP.daypass.DayPass;
import com.example.MMP.daypass.DayPassService;
import com.example.MMP.ptpass.PtPass;
import com.example.MMP.ptpass.PtPassService;
import com.example.MMP.security.UserDetail;
import com.example.MMP.siteuser.SiteUser;
import com.example.MMP.siteuser.SiteUserService;
import com.example.MMP.userPass.UserDayPass;
import com.example.MMP.userPass.UserDayPassService;
import com.example.MMP.userPass.UserPtPass;
import com.example.MMP.userPass.UserPtPassService;
import com.example.MMP.usercoupon.UserCoupon;
import com.example.MMP.usercoupon.UserCouponService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.json.JSONParser;
import org.apache.tomcat.util.json.ParseException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final PtPassService ptPassService;
    private final SiteUserService siteUserService;
    private final UserPtPassService userPtPassService;
    private final DayPassService dayPassService;
    private final UserDayPassService userDayPassService;
    private final UserCouponService userCouponService;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/confirm")
    public ResponseEntity<JSONObject> confirmPayment(@RequestBody String jsonBody) throws Exception {

        JSONParser parser = new JSONParser(jsonBody);
        String orderId;
        String amount;
        String paymentKey;
        try {
            Map<String, String> requestData = (Map<String, String>) parser.parse();

            // 클라이언트에서 받은 JSON 요청 바디입니다.
//            JSONObject requestData = (JSONObject) parser.parse();
            paymentKey = (String) requestData.get("paymentKey");
            orderId = (String) requestData.get("orderId");
            amount = (String) requestData.get("amount");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        ;
        JSONObject obj = new JSONObject();
        obj.put("orderId", orderId);
        obj.put("amount", amount);
        obj.put("paymentKey", paymentKey);

        // TODO: 개발자센터에 로그인해서 내 결제위젯 연동 키 > 시크릿 키를 입력하세요. 시크릿 키는 외부에 공개되면 안돼요.
        // @docs https://docs.tosspayments.com/reference/using-api/api-keys
        String widgetSecretKey = "test_gsk_docs_OaPz8L5KdmQXkzRz3y47BMw6";

        // 토스페이먼츠 API는 시크릿 키를 사용자 ID로 사용하고, 비밀번호는 사용하지 않습니다.
        // 비밀번호가 없다는 것을 알리기 위해 시크릿 키 뒤에 콜론을 추가합니다.
        // @docs https://docs.tosspayments.com/reference/using-api/authorization#%EC%9D%B8%EC%A6%9D
        Base64.Encoder encoder = Base64.getEncoder();
        byte[] encodedBytes = encoder.encode((widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));
        String authorizations = "Basic " + new String(encodedBytes);

        // 결제 승인 API를 호출하세요.
        // 결제를 승인하면 결제수단에서 금액이 차감돼요.
        // @docs https://docs.tosspayments.com/guides/payment-widget/integration#3-결제-승인하기
        URL url = new URL("https://api.tosspayments.com/v1/payments/confirm");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestProperty("Authorization", authorizations);
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);


        OutputStream outputStream = connection.getOutputStream();
        outputStream.write(obj.toString().getBytes("UTF-8"));

        int code = connection.getResponseCode();
        boolean isSuccess = code == 200;

        InputStream responseStream = isSuccess ? connection.getInputStream() : connection.getErrorStream();

        // TODO: 결제 성공 및 실패 비즈니스 로직을 구현하세요.
        Reader reader = new InputStreamReader(responseStream, StandardCharsets.UTF_8);
        JSONObject jsonObject = (JSONObject) parser.parse();
        responseStream.close();

        return ResponseEntity.status(code).body(jsonObject);
    }


    @RequestMapping(value = "/success/{name}", method = RequestMethod.GET)
    public String paymentRequest(@PathVariable("name") String name,
                                 @AuthenticationPrincipal UserDetail userDetail,
                                 @RequestParam(value = "coupon") Long coupon) throws Exception {

        SiteUser siteUser = siteUserService.findByUserName(userDetail.getUsername());
        PtPass ptPass = ptPassService.findByName(name);
        if (ptPass != null) {
            UserPtPass userPtPass = userPtPassService.UserPtAdd(ptPass.getPassName(), ptPass.getPassTitle(), ptPass.getPassCount(), ptPass.getPassPrice(), ptPass.getPassDays(), siteUser);
        } else {
            DayPass dayPass = dayPassService.findByName(name);
            UserDayPass userDayPass = userDayPassService.UserDayadd(dayPass.getPassName(), dayPass.getPassTitle(), dayPass.getPassPrice(), dayPass.getPassDays(), siteUser);
        }
        if(coupon != 0){
            UserCoupon userCoupon = userCouponService.findById(coupon);

            siteUser.getUserCouponList().remove(userCoupon);
            siteUserService.save(siteUser);
            userCouponService.delete(userCoupon);
        }
        return "order/success";
    }

    @RequestMapping(value = "/checkout/{name}", method = RequestMethod.GET)
    public String index(HttpServletRequest request, Model model, @PathVariable("name") String name, @AuthenticationPrincipal UserDetail userDetail) throws Exception {
        PtPass ptPass = ptPassService.findByName(name);
        if (ptPass != null) {
            model.addAttribute("Pass", ptPass);
        } else {
            DayPass dayPass = dayPassService.findByName(name);
            model.addAttribute("Pass", dayPass);
        }
        SiteUser siteUser = siteUserService.findByUserName(userDetail.getUsername());
        List<UserCoupon> couponList = siteUser.getUserCouponList();
        if(couponList.isEmpty()){
            model.addAttribute("couponList",null);
        }
        model.addAttribute("couponList",couponList);
        model.addAttribute("user", siteUser);
        return "order/checkout";
    }

    /**
     * 인증실패처리
     *
     * @param request
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/fail", method = RequestMethod.GET)
    public String failPayment(HttpServletRequest request, Model model) throws Exception {
        String failCode = request.getParameter("code");
        String failMessage = request.getParameter("message");

        model.addAttribute("code", failCode);
        model.addAttribute("message", failMessage);

        return "order/fail";
    }
}
