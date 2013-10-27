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

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;

@Controller
@RequestMapping("/jlgj/")
public class WeiXinController {
    private final String TOKEN = "Jlgj20131021";

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    @ResponseBody
    public String get(HttpServletRequest request, ModelMap model) {
        System.out.println("xiongbo get");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        System.out.println("xiongbo signature : " + signature + " timestamp : " + timestamp + " nonce : " + nonce + " echostr : " + echostr);

        if (null != timestamp && null != nonce && null != signature) {
            if (WeiXin.access(TOKEN, signature, timestamp, nonce)) {
                System.out.println("xiongbo success");
                //TODO 校验通过
                return echostr;
            } else {
                System.out.println("xiongbo failed");
                // TODO 校验失败
            }
            return "get";
        }

        return "get";
    }

    @RequestMapping(value = "/index", method = RequestMethod.POST)
    @ResponseBody
    public String post(HttpServletRequest request, ModelMap model) {
        System.out.println("xiongbo post");
        String signature = request.getParameter("signature");
        String timestamp = request.getParameter("timestamp");
        String nonce = request.getParameter("nonce");
        String echostr = request.getParameter("echostr");

        System.out.println("xiongbo signature : " + signature + " timestamp : " + timestamp + " nonce : " + nonce + " echostr : " + echostr);

        if (null != timestamp && null != nonce && null != signature) {
            if (WeiXin.access(TOKEN, signature, timestamp, nonce)) {
                System.out.println("xiongbo success");
                //TODO 校验通过
                try {
                    WxRecvMsg msg = WeiXin.recv(request.getInputStream());
                    System.out.println("xiongbo msgid:"+msg.getMsgId()+" to:"+msg.getToUser()+
                            " from:"+msg.getFromUser()+" type:"+msg.getMsgType()+" createtime:"+msg.getCreateDt());
                    WxSendMsg sendMsg = WeiXin.builderSendByRecv(msg);

                    // 微信事件消息, 关注/取消关注/菜单...
                    if(msg instanceof WxRecvEventMsg) {
                        WxRecvEventMsg m = (WxRecvEventMsg) msg;
                        String event = m.getEvent();
                        // 有人关注微信帐号
                        if("subscribe".equals(event)) {
                            String content = "欢迎关注xxx";
                            sendMsg = new WxSendTextMsg(sendMsg, content);
                            ByteArrayOutputStream os=new ByteArrayOutputStream();
                            WeiXin.send(sendMsg, os);
                            // 构建文本消息进行发送
                            // 发送回微信
//                            WeiXin.send(sendMsg, getResponse().getOutputStream());
                            return new String(os.toByteArray(), Charset.forName("iso-8859-1"));
                        }else if("unsubscribe".equals(event)){

                        }
                    }

                    if(msg instanceof WxRecvTextMsg){
                        WxRecvTextMsg recvTextMsg=(WxRecvTextMsg)msg;
                        String content = "欢迎关注xxx111";
                        // 构建文本消息进行发送
                        sendMsg = new WxSendTextMsg(sendMsg, content);
                        // 发送回微信
                        ByteArrayOutputStream os=new ByteArrayOutputStream();
                        WeiXin.send(sendMsg, os);
                        return new String(os.toByteArray(), Charset.forName("iso-8859-1"));
                    }
                } catch (IOException ioe) {
                    System.out.println("xiongbo ioe xception");
                    ioe.printStackTrace();
                }catch (JDOMException joe){
                    System.out.println("xiongbo joi xception");
                    joe.printStackTrace();
                }





//                return echostr;
            } else {
                System.out.println("xiongbo failed");
                // TODO 校验失败
            }
            return "post";
        }

        return "post";
    }
}
