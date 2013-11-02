package net.xiaoqu.weixin;

import net.xiaoqu.weixin.util.WeiXin;
import net.xiaoqu.weixin.vo.recv.WxRecvEventMsg;
import net.xiaoqu.weixin.vo.recv.WxRecvMsg;
import net.xiaoqu.weixin.vo.recv.WxRecvTextMsg;
import net.xiaoqu.weixin.vo.send.WxSendMsg;
import net.xiaoqu.weixin.vo.send.WxSendTextMsg;
import org.jdom.JDOMException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.ResourceBundle;

@Controller
@RequestMapping("/jlgj/")
public class WeiXinController {
//    @Autowired
//    org.springframework.context.support.ResourceBundleMessageSource resourceBundleMessageSource;
    private final String TOKEN = "Jlgj20131021";

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public String get(HttpServletRequest request, ModelMap model) {
        // System.out.println("xiongbo get");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        // System.out.println("xiongbo signature : " + signature + " timestamp : " + timestamp + " nonce : " + nonce + " echostr : " + echostr);

        if (null != timestamp && null != nonce && null != signature) {
            if (WeiXin.access(TOKEN, signature, timestamp, nonce)) {
                // 校验通过
                return echostr;
            } else {
                // 校验失败
            }
            return "get";
        }

        return "get";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String post(HttpServletRequest request, ModelMap model) {
        Locale locale = RequestContextUtils.getLocaleResolver(request).resolveLocale(request);

        ResourceBundle myResources =
                ResourceBundle.getBundle("messages", locale);
//        String vaule = resourceBundleMessageSource.getMessage("welcome",null, locale);
        String value= myResources.getString("welcome");

        System.out.println("xiongbo post");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        System.out.println("xiongbo signature : " + signature + " timestamp : " + timestamp + " nonce : " + nonce + " echostr : " + echostr);

        if (null != timestamp && null != nonce && null != signature) {
            if (WeiXin.access(TOKEN, signature, timestamp, nonce)) {
                System.out.println("xiongbo success");
                //校验通过
                try {
                    WxRecvMsg msg = WeiXin.recv(request.getInputStream());
                    System.out.println("xiongbo msgid:" + msg.getMsgId() + " to:" + msg.getToUser() +
                            " from:" + msg.getFromUser() + " type:" + msg.getMsgType() + " createtime:" + msg.getCreateDt());
                    WxSendMsg sendMsg = WeiXin.builderSendByRecv(msg);

                    // 事件推送, 关注/取消关注
                    if (msg instanceof WxRecvEventMsg) {
                        WxRecvEventMsg m = (WxRecvEventMsg) msg;
                        String event = m.getEvent();
                        // 有人关注微信帐号
                        if ("subscribe".equals(event)) {
                            String content = "欢迎关注xxx";
                            sendMsg = new WxSendTextMsg(sendMsg, content);
                            ByteArrayOutputStream os = new ByteArrayOutputStream();
                            WeiXin.send(sendMsg, os);
                            return new String(os.toByteArray(), Charset.forName("iso-8859-1"));
                        } else if ("unsubscribe".equals(event)) {

                        }
                    }

                    // 消息推送，文本/图片/地理位置/链接/
                    if (msg instanceof WxRecvTextMsg) {
                        WxRecvTextMsg recvTextMsg = (WxRecvTextMsg) msg;
                        String content = "（●ω●） 欢迎关注xxx'111' 15800488118/r/n\r\n 18616533721"+value;
                        // 构建文本消息进行发送
                        sendMsg = new WxSendTextMsg(sendMsg, content);
//                        WxSendNewsMsg newsMsg=new WxSendNewsMsg(sendMsg);
//                        newsMsg.addItem("title","description","https://mp.weixin.qq.com/mpres/htmledition/images/pic_myapp_func_02_zh_CN.jpg"
//                        ,"http://115.29.175.143/xiaoqu/jlgj/index.do");

                        // 发送回微信
                        ByteArrayOutputStream os = new ByteArrayOutputStream();
                        WeiXin.send(sendMsg, os);
                        return new String(os.toByteArray(), Charset.forName("iso-8859-1"));
                    }
                } catch (IOException ioe) {
                    System.out.println("xiongbo ioe xception");
                    ioe.printStackTrace();
                } catch (JDOMException joe) {
                    System.out.println("xiongbo joi xception");
                    joe.printStackTrace();
                }

            } else {
                System.out.println("xiongbo failed");
                // TODO 校验失败
            }
            return "post";
        }

        return "post";
    }
}
