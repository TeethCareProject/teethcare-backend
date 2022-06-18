package com.teethcare.utils;

public class MailTemplateUtils {
    public static String getStaffCreatingPasswordEmail(String username, String password, String fwdLink) {
        return "<!DOCTYPE html><html xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:o=\"urn:schemas-microsoft-com:office:office\" lang=\"en\"><head><title></title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\"><meta name=\"viewport\" content=\"width=device-width,initial-scale=1\"><!--[if mso]><xml><o:OfficeDocumentSettings><o:PixelsPerInch>96</o:PixelsPerInch><o:AllowPNG/></o:OfficeDocumentSettings></xml><![endif]--><!--[if !mso]><!--><link href=\"https://fonts.googleapis.com/css?family=Open+Sans\" rel=\"stylesheet\" type=\"text/css\"><link href=\"https://fonts.googleapis.com/css?family=Cabin\" rel=\"stylesheet\" type=\"text/css\"><!--<![endif]--><style> *{box-sizing:border-box}body{margin:0;padding:0}a[x-apple-data-detectors]{color:inherit!important;text-decoration:inherit!important}#MessageViewBody a{color:inherit;text-decoration:none}p{line-height:inherit}.desktop_hide,.desktop_hide table{mso-hide:all;display:none;max-height:0;overflow:hidden}@media (max-width:620px){.desktop_hide table.icons-inner{display:inline-block!important}.icons-inner{text-align:center}.icons-inner td{margin:0 auto}.row-content{width:100%!important}.image_block img.big,td.content_blocks{width:auto!important}.column .border,.mobile_hide{display:none}table{table-layout:fixed!important}.stack .column{width:100%;display:block}.mobile_hide{min-height:0;max-height:0;max-width:0;overflow:hidden;font-size:0}.desktop_hide,.desktop_hide table{display:table!important;max-height:none!important}.row-3 .column-1{border-right:14px solid #d0d6f1;border-left:14px solid #d0d6f1}} </style></head><body style=\"background-color:#d9dffa;margin:0;padding:0;-webkit-text-size-adjust:none;text-size-adjust:none\"><table class=\"nl-container\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;background-color:#d9dffa\"><tbody><tr><td><table class=\"row row-1\" align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;background-color:#d0d6f1\"><tbody><tr><td><table class=\"row-content stack\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;color:#000;width:600px\" width=\"600\"><tbody><tr><td class=\"column column-1\" width=\"100%\" style=\"mso-table-lspace:0;mso-table-rspace:0;font-weight:400;text-align:left;vertical-align:top;padding-top:20px;padding-bottom:0;border-top:0;border-right:0;border-bottom:0;border-left:0\"><table class=\"image_block\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0\"><tr><td style=\"width:100%;padding-right:0;padding-left:0\"><div align=\"center\" style=\"line-height:10px\"><img class=\"big\" src=\"https://d1oco4z2z1fhwp.cloudfront.net/templates/default/3991/animated_header.gif\" style=\"display:block;height:auto;border:0;width:600px;max-width:100%\" width=\"600\" alt=\"Card Header with Border and Shadow Animated\" title=\"Card Header with Border and Shadow Animated\"></div></td></tr></table></td></tr></tbody></table></td></tr></tbody></table><table class=\"row row-2 mobile_hide\" align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;background-color:#d9dffa;background-image:url(https://d1oco4z2z1fhwp.cloudfront.net/templates/default/3991/body_background_2.png);background-position:top center;background-repeat:repeat\"><tbody><tr><td><table class=\"row-content stack\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;color:#000;width:600px\" width=\"600\"><tbody><tr><td class=\"column column-1\" width=\"100%\" style=\"mso-table-lspace:0;mso-table-rspace:0;font-weight:400;text-align:left;vertical-align:top;border-right:0 solid #d0d6f1;padding-left:50px;padding-right:50px;padding-top:15px;padding-bottom:15px;border-top:0;border-bottom:0;border-left:0\"><table class=\"text_block\" width=\"100%\" border=\"0\" cellpadding=\"10\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;word-break:break-word\"><tr><td><div style=\"font-family:sans-serif\"><div class=\"txtTinyMce-wrapper\" style=\"font-size:14px;mso-line-height-alt:16.8px;color:#506bec;line-height:1.2;font-family:Helvetica Neue,Helvetica,Arial,sans-serif\"><p style=\"margin:0;font-size:14px\"><strong><span style=\"font-size:38px;\">Activate your account</span></strong></p></div></div></td></tr></table><table class=\"text_block\" width=\"100%\" border=\"0\" cellpadding=\"10\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;word-break:break-word\"><tr><td><div style=\"font-family:sans-serif\"><div class=\"txtTinyMce-wrapper\" style=\"font-size:14px;mso-line-height-alt:16.8px;color:#40507a;line-height:1.2;font-family:Helvetica Neue,Helvetica,Arial,sans-serif\"><p style=\"margin:0;font-size:14px\"><span style=\"font-size:16px;\">Hey, you were added into new Clinic.</span></p></div></div></td></tr></table><table class=\"text_block\" width=\"100%\" border=\"0\" cellpadding=\"10\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;word-break:break-word\"><tr><td><div style=\"font-family:sans-serif\"><div class=\"txtTinyMce-wrapper\" style=\"font-size:14px;mso-line-height-alt:16.8px;color:#40507a;line-height:1.2;font-family:Helvetica Neue,Helvetica,Arial,sans-serif\"><p style=\"margin:0;font-size:14px\"><span style=\"font-size:16px;\">Username: <b>" + username + "</b></span></p><p style=\"margin:0;font-size:14px\"><span style=\"font-size:16px;\">Password: <b>" + password + "</b></span></p></div></div></td></tr></table><table class=\"button_block\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0\"><tr><td style=\"padding-bottom:20px;padding-left:10px;padding-right:10px;padding-top:20px;text-align:left\"><!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"" + fwdLink + "\" style=\"height:48px;width:196px;v-text-anchor:middle;\" arcsize=\"34%\" stroke=\"false\" fillcolor=\"#506bec\"><w:anchorlock/><v:textbox inset=\"5px,0px,0px,0px\"><center style=\"color:#ffffff; font-family:Arial, sans-serif; font-size:15px\"><![endif]--> <a href=\"" + fwdLink + "\" target=\"_blank\" style=\"text-decoration:none;display:inline-block;color:#ffffff;background-color:#506bec;border-radius:16px;width:auto;border-top:0px solid TRANSPARENT;font-weight:400;border-right:0px solid TRANSPARENT;border-bottom:0px solid TRANSPARENT;border-left:0px solid TRANSPARENT;padding-top:8px;padding-bottom:8px;font-family:Helvetica Neue, Helvetica, Arial, sans-serif;text-align:center;mso-border-alt:none;word-break:keep-all;\"><span style=\"padding-left:25px;padding-right:20px;font-size:15px;display:inline-block;letter-spacing:normal;\"><span style=\"font-size: 16px; line-height: 2; word-break: break-word; mso-line-height-alt: 32px;\"><span style=\"font-size: 15px; line-height: 30px;\" data-mce-style=\"font-size: 15px; line-height: 30px;\"><strong>CHANGE PASSWORD</strong></span></span></span></a> <!--[if mso]></center></v:textbox></v:roundrect><![endif]--></td></tr></table><table class=\"text_block\" width=\"100%\" border=\"0\" cellpadding=\"10\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;word-break:break-word\"><tr><td><div style=\"font-family:sans-serif\"><div class=\"txtTinyMce-wrapper\" style=\"font-size:14px;mso-line-height-alt:16.8px;color:#40507a;line-height:1.2;font-family:Helvetica Neue,Helvetica,Arial,sans-serif\"><p style=\"margin:0;font-size:14px\"> <strong><span style=\"font-size:14px;\"><a href=\"" + fwdLink + "\" target=\"_blank\" style=\"text-decoration: underline; color: #40507a;\" rel=\"noopener\">Having trouble?</a></span></strong></p></div></div></td></tr></table><table class=\"text_block\" width=\"100%\" border=\"0\" cellpadding=\"10\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;word-break:break-word\"><tr><td><div style=\"font-family:sans-serif\"><div class=\"txtTinyMce-wrapper\" style=\"font-size:14px;mso-line-height-alt:16.8px;color:#40507a;line-height:1.2;font-family:Helvetica Neue,Helvetica,Arial,sans-serif\"><p style=\"margin:0;font-size:14px\">You must create a password to login!</p></div></div></td></tr></table></td></tr></tbody></table></td></tr></tbody></table><table class=\"row row-3 desktop_hide\" align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;mso-hide:all;display:none;max-height:0;overflow:hidden;background-color:#d9dffa;background-image:url(https://d1oco4z2z1fhwp.cloudfront.net/templates/default/3991/body_background_2.png);background-position:top center;background-repeat:repeat\"><tbody><tr><td><table class=\"row-content stack\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;mso-hide:all;display:none;max-height:0;overflow:hidden;color:#000;width:600px\" width=\"600\"><tbody><tr><td class=\"column column-1\" width=\"100%\" style=\"mso-table-lspace:0;mso-table-rspace:0;font-weight:400;text-align:left;vertical-align:top\"><table width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;mso-hide:all;display:none;max-height:0;overflow:hidden\"><tr><td class=\"border\" style=\"width:14px;background-color:#d0d6f1\">&nbsp;</td><td class=\"content_blocks\" style=\"padding-left:25px;padding-right:25px;padding-top:0;padding-bottom:0;border-top:0;border-bottom:0;width:572px\"><table class=\"text_block\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;word-break:break-word;mso-hide:all;display:none;max-height:0;overflow:hidden\"><tr><td style=\"padding-bottom:10px;padding-left:10px;padding-right:10px;padding-top:25px\"><div style=\"font-family:sans-serif\"><div class=\"txtTinyMce-wrapper\" style=\"font-size:14px;mso-line-height-alt:16.8px;color:#506bec;line-height:1.2;font-family:Helvetica Neue,Helvetica,Arial,sans-serif\"><p style=\"margin:0;font-size:14px\"><span style=\"font-size:30px;\"><strong><span style>Activate your account</span></strong></span></p></div></div></td></tr></table><table class=\"text_block\" width=\"100%\" border=\"0\" cellpadding=\"10\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;word-break:break-word;mso-hide:all;display:none;max-height:0;overflow:hidden\"><tr><td><div style=\"font-family:sans-serif\"><div class=\"txtTinyMce-wrapper\" style=\"font-size:14px;mso-line-height-alt:16.8px;color:#40507a;line-height:1.2;font-family:Helvetica Neue,Helvetica,Arial,sans-serif\"><p style=\"margin:0;font-size:14px\"><span style=\"font-size:15px;\">Hey, you were added into new Clinic.</span></p> </div></div></td></tr></table><table class=\"text_block\" width=\"100%\" border=\"0\" cellpadding=\"10\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;word-break:break-word;mso-hide:all;display:none;max-height:0;overflow:hidden\"><tr><td><div style=\"font-family:sans-serif\"><div class=\"txtTinyMce-wrapper\" style=\"font-size:14px;mso-line-height-alt:16.8px;color:#40507a;line-height:1.2;font-family:Helvetica Neue,Helvetica,Arial,sans-serif\"><p style=\"margin:0;font-size:14px\"><span style=\"font-size:15px;\">Let’s create your own password!</span></p></div></div></td></tr></table><table class=\"button_block\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;mso-hide:all;display:none;max-height:0;overflow:hidden\"><tr><td style=\"padding-bottom:20px;padding-left:10px;padding-right:10px;padding-top:20px;text-align:left\"> <!--[if mso]><v:roundrect xmlns:v=\"urn:schemas-microsoft-com:vml\" xmlns:w=\"urn:schemas-microsoft-com:office:word\" href=\"" + fwdLink + "\" style=\"height:48px;width:196px;v-text-anchor:middle;\" arcsize=\"34%\" stroke=\"false\" fillcolor=\"#506bec\"><w:anchorlock/><v:textbox inset=\"5px,0px,0px,0px\"><center style=\"color:#ffffff; font-family:Arial, sans-serif; font-size:15px\"><![endif]--> <a href=\"" + fwdLink + "\" target=\"_blank\" style=\"text-decoration:none;display:inline-block;color:#ffffff;background-color:#506bec;border-radius:16px;width:auto;border-top:0px solid TRANSPARENT;font-weight:400;border-right:0px solid TRANSPARENT;border-bottom:0px solid TRANSPARENT;border-left:0px solid TRANSPARENT;padding-top:8px;padding-bottom:8px;font-family:Helvetica Neue, Helvetica, Arial, sans-serif;text-align:center;mso-border-alt:none;word-break:keep-all;\"><span style=\"padding-left:25px;padding-right:20px;font-size:15px;display:inline-block;letter-spacing:normal;\"><span style=\"font-size: 16px; line-height: 2; word-break: break-word; mso-line-height-alt: 32px;\"><span style=\"font-size: 15px; line-height: 30px;\" data-mce-style=\"font-size: 15px; line-height: 30px;\"><strong>CREATE PASSWORD</strong></span></span></span></a> <!--[if mso]></center></v:textbox></v:roundrect><![endif]--></td></tr></table><table class=\"text_block\" width=\"100%\" border=\"0\" cellpadding=\"10\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;word-break:break-word;mso-hide:all;display:none;max-height:0;overflow:hidden\"><tr><td><div style=\"font-family:sans-serif\"><div class=\"txtTinyMce-wrapper\" style=\"font-size:14px;mso-line-height-alt:16.8px;color:#40507a;line-height:1.2;font-family:Helvetica Neue,Helvetica,Arial,sans-serif\"><p style=\"margin:0;font-size:14px\"><span style=\"font-size:13px;\"><strong><span style><a href=\"" + fwdLink + "\" target=\"_blank\" style=\"text-decoration: underline; color: #40507a;\" rel=\"noopener\">Having trouble?</a></span></strong></span></p></div></div></td></tr></table><table class=\"text_block\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;word-break:break-word;mso-hide:all;display:none;max-height:0;overflow:hidden\"><tr><td style=\"padding-bottom:25px;padding-left:10px;padding-right:10px;padding-top:10px\"><div style=\"font-family:sans-serif\"><div class=\"txtTinyMce-wrapper\" style=\"font-size:14px;mso-line-height-alt:16.8px;color:#40507a;line-height:1.2;font-family:Helvetica Neue,Helvetica,Arial,sans-serif\"><p style=\"margin:0;font-size:14px\"> <span style=\"font-size:13px;\">You must create a password to login!</span></p></div></div></td></tr></table></td><td class=\"border\" style=\"width:14px;background-color:#d0d6f1\">&nbsp;</td></tr></table></td></tr></tbody></table></td></tr></tbody></table><table class=\"row row-4\" align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0\"><tbody><tr><td><table class=\"row-content stack\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;color:#000;width:600px\" width=\"600\"><tbody><tr><td class=\"column column-1\" width=\"100%\" style=\"mso-table-lspace:0;mso-table-rspace:0;font-weight:400;text-align:left;vertical-align:top;padding-top:0;padding-bottom:5px;border-top:0;border-right:0;border-bottom:0;border-left:0\"><table class=\"image_block\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0\"><tr><td style=\"width:100%;padding-right:0;padding-left:0\"><div align=\"center\" style=\"line-height:10px\"><img class=\"big\" src=\"https://d1oco4z2z1fhwp.cloudfront.net/templates/default/3991/bottom_img.png\" style=\"display:block;height:auto;border:0;width:600px;max-width:100%\" width=\"600\" alt=\"Card Bottom with Border and Shadow Image\" title=\"Card Bottom with Border and Shadow Image\"></div></td></tr></table></td></tr></tbody></table></td></tr></tbody> </table><table class=\"row row-5\" align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0\"><tbody><tr><td><table class=\"row-content stack\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;color:#000;width:600px\" width=\"600\"><tbody><tr><td class=\"column column-1\" width=\"100%\" style=\"mso-table-lspace:0;mso-table-rspace:0;font-weight:400;text-align:left;vertical-align:top;padding-left:10px;padding-right:10px;padding-top:10px;padding-bottom:20px;border-top:0;border-right:0;border-bottom:0;border-left:0\"><table class=\"image_block\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0\"><tr><td style=\"padding-bottom:10px;padding-left:60px;padding-right:45px;width:100%\"><div align=\"center\" style=\"line-height:10px\"><a href=\"" + fwdLink + "\" target=\"_blank\" style=\"outline:none\" tabindex=\"-1\"><img src=\"https://d15k2d11r6t6rl.cloudfront.net/public/users/Integrators/BeeProAgency/812445_796351/280755315_568858947933458_6963707826084130171_n.png\" style=\"display:block;height:auto;border:0;width:203px;max-width:100%\" width=\"203\" alt=\"Your Logo\" title=\"Your Logo\"></a></div></td></tr></table><table class=\"text_block\" width=\"100%\" border=\"0\" cellpadding=\"10\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;word-break:break-word\"><tr><td><div style=\"font-family:sans-serif\"><div class=\"txtTinyMce-wrapper\" style=\"font-size:14px;mso-line-height-alt:16.8px;color:#97a2da;line-height:1.2;font-family:Helvetica Neue,Helvetica,Arial,sans-serif\"><p style=\"margin:0;text-align:center;font-size:12px\"><span style=\"font-size:12px;\">Copyright© 2022 Teethcare</span></p></div></div></td></tr></table></td></tr></tbody></table></td></tr></tbody></table> <table class=\"row row-6\" align=\"center\" width=\"100%\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0\"><tbody><tr><td><table class=\"row-content stack\" align=\"center\" border=\"0\" cellpadding=\"0\" cellspacing=\"0\" role=\"presentation\" style=\"mso-table-lspace:0;mso-table-rspace:0;color:#000;width:600px\" width=\"600\"><tbody><tr><td class=\"column column-1\" width=\"100%\" style=\"mso-table-lspace:0;mso-table-rspace:0;font-weight:400;text-align:left;vertical-align:top;padding-top:5px;padding-bottom:5px;border-top:0;border-right:0;border-bottom:0;border-left:0\"></body></html>";
    }
}
