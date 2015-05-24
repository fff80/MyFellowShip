<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="image/jpeg"
	import="java.awt.*,java.awt.image.*,java.util.*,javax.imageio.*"%>
<%!Color getRandColor(int fc, int bc) {//给定范围获得随机颜色
		Random random = new Random();
		if (fc > 255)
			fc = 255;
		if (bc > 255)
			bc = 255;
		int r = fc + random.nextInt(bc - fc);
		int g = fc + random.nextInt(bc - fc);
		int b = fc + random.nextInt(bc - fc);
		return new Color(r, g, b);
	}%>
<%
	try {
		//设置页面不缓存
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);

		// 在内存中创建图象
		int width = 90, height = 20;
		BufferedImage image = new BufferedImage(width, height,
				BufferedImage.TYPE_INT_RGB);

		// 获取图形上下文
		Graphics g = image.getGraphics();

		//生成随机类
		Random random = new Random();

		// 设定背景色
		g.setColor(getRandColor(200, 250));
		g.fillRect(0, 0, width, height);

		//设定字体
		g.setFont(new Font("Times New Roman", Font.PLAIN, 25));

		//画边框
		//g.setColor(new Color());
		//g.drawRect(0,0,width-1,height-1);

		// 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
		g.setColor(getRandColor(160, 200));
		for (int i = 0; i < 155; i++) {
			int x = random.nextInt(width);
			int y = random.nextInt(height);
			int xl = random.nextInt(12);
			int yl = random.nextInt(12);
			g.drawLine(x, y, x + xl, y + yl);
		}
        // 取随机产生的认证码(全部是数字)
	
	    String sRand = "";
		for (int i = 0; i < 4; i++) {
	        String rand = String.valueOf(random.nextInt(10));
			sRand += rand;
			
		// 取随机产生的认证码（数字加字母）
	//	char c[] = new char[62];

	//	for (int i = 97, j = 0; i < 123; i++, j++) {
	//		c[j] = (char) i;
	//	}
	//	for (int o = 65, p = 26; o < 91; o++, p++) {
	//		c[p] = (char) o;
	//	}
	//	for (int m = 48, n = 52; m < 58; m++, n++) {
	//		c[n] = (char) m;
	//	}
	//	String sRand = "";
	//	for (int i = 0; i < 4; i++) {
	//		int x = random.nextInt(62);
	//		String rand = String.valueOf(c[x]);
	//		sRand += rand;
			
			// 将认证码显示到图象中
			g.setColor(new Color(20 + random.nextInt(110), 20 + random
					.nextInt(110), 20 + random.nextInt(110)));
			//调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成
			g.drawString(rand, 20 * i + 7, 20);
		}

		// 将认证码存入SESSION
		session.setAttribute("rand", sRand);

		// 图象生效
		g.dispose();

		// 输出图象到页面
		ImageIO.write(image, "JPEG", response.getOutputStream());
		response.flushBuffer();
		out.clear();
		out = pageContext.pushBody();
	} catch (IllegalStateException e) {
		System.out.println(e.getMessage());
		e.printStackTrace();
	}
%>
