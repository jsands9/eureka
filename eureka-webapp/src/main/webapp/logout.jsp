<%--
  #%L
  Eureka WebApp
  %%
  Copyright (C) 2012 - 2013 Emory University
  %%
  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at
  
       http://www.apache.org/licenses/LICENSE-2.0
  
  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
  #L%
  --%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="/WEB-INF/tlds/template.tld" prefix="template"%>

<template:insert template="/templates/eureka_sidebar.jsp">

	<template:content name="sidebar">
		<img src="${pageContext.request.contextPath}/images/scientific_research.jpg" />
	</template:content>

	<template:content name="content">
		<h3>Logout</h3>
		<div id="msg" class="success">
			<p>You have successfully logged out of Eureka! Clinical Analytics.</p>
			<p>For security reasons, exit your web browser.</p>
		</div>
	</template:content>
</template:insert>
