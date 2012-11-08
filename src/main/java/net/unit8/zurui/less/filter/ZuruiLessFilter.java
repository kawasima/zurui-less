/*
 *  Copyright 2012 kawasima, kawasima1016@gmail.com
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package net.unit8.zurui.less.filter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.lesscss.LessCompiler;
import org.lesscss.LessException;

@WebFilter(filterName="zuruiLessFilter", urlPatterns={"*.less"})
public class ZuruiLessFilter implements Filter {
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;


		String zuruiLess = readZuruiLess();
		String lessPath = request.getServletContext().getRealPath(request.getRequestURI());
		String requestLess = FileUtils.readFileToString(new File(lessPath), "UTF-8");
		LessCompiler lessCompiler = new LessCompiler();
		try {
			String css = lessCompiler.compile(zuruiLess + requestLess);
			res.getWriter().write(css);
		} catch (LessException e) {
			throw new ServletException(e);
		}
	}

	public void destroy() {
	}

	private String readZuruiLess() {
		InputStream lessStream = getClass().getResourceAsStream("/net/unit8/zurui/less/filter/zurui.less");
		return new Scanner(lessStream).useDelimiter("\\A").next();
	}
}
