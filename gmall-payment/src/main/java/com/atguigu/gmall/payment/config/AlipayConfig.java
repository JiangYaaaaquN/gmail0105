package com.atguigu.gmall.payment.config;/* *
 *类名：AlipayConfig
 *功能：基础配置类
 *详细：设置帐户有关信息及返回路径
 *修改日期：2017-04-05
 *说明：
 *以下代码只是为了方便商户测试而提供的样例代码，商户可以根据自己网站的需要，按照技术文档编写,并非一定要使用该代码。
 *该代码仅供学习和研究支付宝接口使用，只是提供一个参考。
 */

import java.io.FileWriter;
import java.io.IOException;

public class AlipayConfig {

//↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓

    // 应用ID,您的APPID，收款账号既是您的APPID对应支付宝账号
    public static String app_id = "2016101300678691";

    // 商户私钥，您的PKCS8格式RSA2私钥
    public static String merchant_private_key = "MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQCcbCoiOAuNu7RrNv3Q+xvuMA0nSRSnWrGMPadCgsBiHn7tClK+4NjkdBzmw9vcUinvfz9lICPsEuKk+JhL+Uxkmmt0fYSM/0nr5Q306KZNOz2n0pibx6vmRlkMHiD4kC0snrmExoH8ZpWg/rnqIb4LcwQ3DSAgLQ9rGGMMCtaf7GPt+Em7USPYzoDUO4ldddnaNtQYmr6UTBQXPEsx9hjrtrGIkr/cjL4NDPpB0LNGCH33oPWOMnbWq61EfVqPjh0Q/YlOy885Zf//E6QPsnxD1HpU4SDBQdzpXFzvIauMKSyPYN+PjzbOqUkzEx66GnjyhWBpfPLdI/owg8u0AVqdAgMBAAECggEAOoOr70kP6WFCSKk1oauN8zg+c2e/NiuTJ2cY3qSS2yGVUY7/DTCHck6NUTYLVQvXbCQpzuaLCZC3AAeO0Tzc+vVZJSafIy+Ms5Xn9qnhLBrxIYqCVCQJhgbi4MmUg+dLPtnRhH3NumGVr7gaTB97PEU2WnGqht7UWbextZuIViI+A8llt86CxDLivkCo8VQT7/HBHEZeCbAeZwpK/nt4O5km9esAcUU+NjH7ivyTDYLiefRU9D3int/LrNRg27rK5At11um1ohCrEHJS5/fwX3maenqYVgofn/Fo1nNQjibbnNFL8zD764iL9ZHsS71035R6/MiekbKJb/Sfp2t8gQKBgQDS2ekBvtgWMqeM0iNM/DJWYrHVFxvgM8l/SVaIGIlEBzsYBuwcGxoezqtcTIaNhJb6I1vhEK3qulPWjTdVt8rcWZZDZtgvJotzQ8dfOmoe+ZExGFwfekXg+NtABGztm2siq+wCQIQofXSuL7x+qTpF/PqK4WAGJYCHj+RSPX7tKQKBgQC96qxQyW3fzPAfOhIlYux/Cga376pzct2izjlx+f1H7t5lw8q+sY7c8Rt4Jb1zDDBl1J3O2yIm+xygPjo4+A2y7IzjDoJCbkLgQdyref0NTyu/+9m5YwbzsIEFcb4RFiGjbvRK55KQC5KBuI2IEsdh0A/MdGb8u3g5695XIO88VQKBgGWsMlFnJ2e80VJ0WhVbXyKMwNfSJtz72+QHBzTlL/ufyP0TCXOcFgJZAgSl2hQPpYXE2x8yHcscQiUpP0+UKrwKbwzdYM2Ltp7ERjVDQ7zf9wVHi9SZ72xGU+MQvz3sPcMgR9PmLFQh+surtScWj0UEcgPaHaWZG9tCpYSKptPRAoGANYstK9WueZOA+3zW0t2SCbsc68zRRJ1U6Wq0/XJau2YYI57Q0XSj75y2fug9AmukVYFdQI29kjnn9zG0Ho2o5xIUIovbo7kMd8d53IqXEqKIlib+WmsV8ayIo61Oxeh0cWx9yYSCaMQqeRI8LOoGdog/fAgxZP3Bxqq+U9hAOsUCgYBYuJVpNBuzMjhMZQO6sAhhWgKZX0qJSOhDQzkZ/PJoxN9QC4524lPiUx9RetxfmVyINgITdd2HgDpaBFLVrUavxKb2SaPufk3YBC6JQkolOqrkiHmtHiBYGx4Iuy1yaXVnyqvmn0V8L77qIQ6A7yZ9pf8NvmvUUFT40vwCBWgYHQ==";

    // 支付宝公钥,查看地址：https://openhome.alipay.com/platform/keyManage.htm 对应APPID下的支付宝公钥。
    public static String alipay_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqqPusBuQGlAXcJnUl1ynEqU9WIJ1tdnl6i5uTZdHGVaVmHimeXMLS7tCim9vrB72sDLm+AT2AtNJQRh/2GgEl6MGUmgPBIrnKqeEZZss1/QDgqnHrowzqAuBcHqTn72RiFqCJ8u4ped10oG6rGr4nTbWGC3QB05XcDRYwmxFJjklnKH0XLRCTxmY/Bz04Zq6OHiI3dMTU2rWB5MNIiJcnbovYh5FdIlanImQvJv2EhLa/O6bGggS0Kq+0QHuFZPbpBmAN4m+eYd5Ho2fYoeJbpHAI+mmHZJw2LQgu5pp9Re8pLQqHWHU5t/HoWiPYE/3FF0JQed+j+rh4pC8tt57pwIDAQAB";

    // 服务器异步通知页面路径  需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String notify_url = "http://luohongling.top/callback/notify_url";

    // 页面跳转同步通知页面路径 需http://格式的完整路径，不能加?id=123这类自定义参数，必须外网可以正常访问
    public static String return_url = "http://payment.gmall.com:8087/alipay/callback/return";

    // 签名方式
    public static String sign_type = "RSA2";

    // 字符编码格式
    public static String charset = "utf-8";

    // 支付宝网关
    public static String gatewayUrl = "https://openapi.alipaydev.com/gateway.do";

    // 支付宝网关
    public static String log_path = "C:\\";


//↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    /**
     * 写日志，方便测试（看网站需求，也可以改成把记录存入数据库）
     * @param sWord 要写入日志里的文本内容
     */
    public static void logResult(String sWord) {
        FileWriter writer = null;
        try {
            writer = new FileWriter(log_path + "alipay_log_" + System.currentTimeMillis()+".txt");
            writer.write(sWord);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}


