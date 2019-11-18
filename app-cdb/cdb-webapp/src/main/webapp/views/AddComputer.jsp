<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="fr.excilys.cdb.api.dto.Computer"%>
<%@ page isELIgnored ="false" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<!DOCTYPE html>
<html>
	<head>
		<title>Computer Database</title>
		<meta name="viewport" content="width=device-width, initial-scale=1.0">
		<!-- Bootstrap -->
		<link href="<c:url value="/resources/css/bootstrap.min.css"/>" rel="stylesheet" media="screen">
		<link href="<c:url value="/resources/css/font-awesome.css"/>" rel="stylesheet" media="screen">
		<link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet" media="screen">
		
		<script src="<c:url value="/resources/js/jquery.min.js"/>"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/moment.js/2.24.0/moment.min.js"></script>
		<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery-validate/1.19.1/jquery.validate.min.js"></script>
		
 	</head>
<body>
<header class="navbar navbar-inverse navbar-fixed-top">
        <div class="container">
            <a class="navbar-brand" href="dashboard"> Application - Computer Database </a>
        </div>
    </header>
    <section id="main">
        <div class="container">
            <div class="row">
                <div class="col-xs-8 col-xs-offset-2 box">
                    <h1><spring:message code="addComputer.title"/></h1>
                    <form id="form" action="addComputer" method="POST" >
                        <fieldset>
                            <div class="form-group">
                            	
                            		<c:if test="${messages.success != null}">
	                            		<div class="alert alert-success">
	                					<span id="success"><strong>Success!</strong> <spring:message code="addComputer.messageSuccess"/></span>
	                					</div>
           							</c:if>
           							<c:if test="${messages.fail != null}">
	                            		<div class="alert alert-warning">
  										<strong>Warning!</strong>
  										<span id="fail" style="color: blue;"> <spring:message code="addComputer.messageFail"/></span>
										</div>
           							</c:if>
                            	
                                <label for="computerName"><spring:message code="addComputer.name"/></label>
                                <input type="text" class="form-control" id="computerName" name="name"  value="${computer.name}" placeholder="<spring:message code="addComputer.name"/>" required>
                                <c:if test="${messages.name != null}">
                					<span id="errorName" style="color: red;"><spring:message code="addComputer.messageName"/></span>
           						</c:if>
           						<span style="color: blue;" id="spanName"></span>
                            </div>
                            <div class="form-group">
                                <label for="introduced"><spring:message code="addComputer.introducedDate"/></label>
                                <input type="date" class="form-control" id="introduced" name="introduced" value="${computer.introduced}" placeholder="<spring:message code="addComputer.introducedDate"/>">
                                <c:if test="${messages.introduced != null}">
                					<span style="color: red;" id="errorIntroduced"><spring:message code="addComputer.messageIntroduced"/></span>
           						</c:if>
                            </div>
                            <div class="form-group">
                                <label for="discontinued"><spring:message code="addComputer.discontinuedDate"/></label>
                                <input type="date" class="form-control" id="discontinued" name="discontinued" value="${computer.discontinued}" placeholder="<spring:message code="addComputer.discontinuedDate"/>">
                            	<span style="color: blue;" id="spanIntro"></span>
                            </div>
                            <div class="form-group">
                                <label for="companyId"><spring:message code="addComputer.company"/></label>
	                                <select class="form-control" id="companyId" name="idCompany">
	                                    <option value="${computer.idCompany}">${computer.nameCompany}</option>
	                                    <c:forEach var="item" items="${companies}">
	                                    <option value="${item.id}">${item.id} -- ${item.name}</option>
	                                    </c:forEach>
	                                </select>
	                                <c:if test="${messages.company != null}">
                						<span style="color: red;"><spring:message code="addComputer.messageCompany"/></span>
           							</c:if>
                                
                            </div>                  
                        </fieldset>
                        <div class="actions pull-right">
                            <input type="submit" id="submit" value="<spring:message code="addComputer.btnAdd"/>" class="btn btn-primary">
                            <spring:message code="addComputer.or"/>
                            <a href="dashboard?number=${1}&size=${10}" class="btn btn-default"><spring:message code="addComputer.btnCancel"/></a>
                        </div>
                    </form> 
                </div>
            </div>
        </div>
    </section>
    <script type="text/javascript" src="<c:url value="/resources/js/validation.js"/>"></script>
</body>
</html>