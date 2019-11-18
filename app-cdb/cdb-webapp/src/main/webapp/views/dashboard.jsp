<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="fr.excilys.cdb.api.dto.Computer"%>
<%@page import="fr.excilys.cdb.api.dto.Pagination"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ page isELIgnored="false"%>
<!-- control, iterations, tests, variables -->
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>


<!DOCTYPE html>
<html>
<head>
<title>Computer Database</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta charset="utf-8">
<!-- Bootstrap -->
<link href="<c:url value="/resources/css/bootstrap.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/font-awesome.css"/>" rel="stylesheet" media="screen">
<link href="<c:url value="/resources/css/main.css"/>" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard"> Application -
				Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">
				<c:out value="${fn:length(pagination.computers)}"></c:out>
				<spring:message code="dashboard.numberOfComputers"/>
			</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="dashboard" method="GET"
						class="form-inline">
						<input type="search" id="searchbox" name="name"
							class="form-control" placeholder="<spring:message code="dashboard.placeholderSearch"/>" value ="${name}"/>
						 <input
							type="submit" id="searchsubmit" value="<spring:message code="dashboard.btnSearch"/>"
							class="btn btn-primary" />
					</form>
				</div>

			</div> 
			<div class="col-md-6 select-outline">
				<form id="sortForm" action="dashboard" method="GET">
					<div class="col-md-4">
						<select class="form-control" name="property">
							<option value="" selected><spring:message code="dashboard.orderBy"/></option>
							<option value='name'
								<c:if test="${sort.property == 'name'}">selected="selected"</c:if>><spring:message code="dashboard.computerName"/></option>
							<option value='company.name'
								<c:if test="${sort.property == 'company.name'}">selected="selected"</c:if>><spring:message code="dashboard.companyName"/></option>
						</select>
					</div>
					<div class="col-md-4">
						<select class="form-control" name="order">
							<option value="" selected><spring:message code="dashboard.type"/></option>
							<option value="ASC"
								<c:if test="${sort.order == 'ASC'}">selected="selected"</c:if>>ASC</option>
							<option value="DESC"
								<c:if test="${sort.order == 'DESC'}">selected="selected"</c:if>>DESC</option>
						</select>
					</div>
					<input type="submit" id="searchsubmit" value="<spring:message code="dashboard.btnSubmit"/>"
						class="btn btn-primary" />
				</form>
			</div>
			<div class="pull-right">
				<a class="btn btn-success" id="addComputer" href="<c:url value="addComputer"/>"><spring:message code="dashboard.btnAddComputer"/></a> <a class="btn btn-default" id="editComputer" href="#"
					onclick="$.fn.toggleEditMode();"><spring:message code="dashboard.btnEdit"/></a>
			</div>

		</div>
		<form id="deleteForm" action="deleteComputers" method="POST">
			<input type="hidden" name="selection" value="${computer.id}">
		</form>
		<div class="container" style="margin-top: 10px;">
			<table class="table table-striped table-bordered">
				<thead>
					<tr>
						<!-- Variable declarations for passing labels as parameters -->
						<!-- Table header for Computer Name -->

						<th class="editMode" style="width: 60px; height: 22px;"><input
							type="checkbox" id="selectall" /> <span
							style="vertical-align: top;"> - <a href="#"
								id="deleteSelected" onclick="$.fn.deleteSelected();"> <i
									class="fa fa-trash-o fa-lg"></i>
							</a>
						</span></th>
						<th><spring:message code="dashboard.name"/></th>
						<th><spring:message code="dashboard.introducedDate"/></th>
						<th><spring:message code="dashboard.discontinuedDate"/></th>
						<th><spring:message code="dashboard.company"/></th>

					</tr>
				</thead>
				<tbody id="results">
					<c:forEach var="item" items="${pagination.computers}">
						<tr>
							<td class="editMode"><input type="checkbox" name="cb"
								class="cb" value="${item.id}"></td>
							<td><a href="editComputer?id=${item.id}" onclick="">
									${item.name }</a></td>
							<td>${item.introduced}</td>
							<td>${item.discontinued}</td>
							<td>${item.nameCompany}</td>
						</tr>
					</c:forEach>
				</tbody>

			</table>
		</div>
	</section>

	<footer class="navbar-fixed-bottom">
		<div class="container text-center">
			<ul class="pagination">
				<c:if test="${pagination.currentPage != 1}">
					<li class="page-item"><a class="page-link"
						href="<c:url value="dashboard?number=${pagination.currentPage - 1}&size=${pagination.size}&name=${name}&property=${sort.property}&order=${sort.order}"/>"><spring:message code="dashboard.previous"/></a>
					</li>
				</c:if>
				<c:forEach begin="${pagination.startPage}" end="${pagination.endPage}" var="i">
					<c:choose>
						<c:when test="${pagination.currentPage eq i}">
							<li class="page-item active"><a class="page-link"> ${i}
									<span class="sr-only">(current)</span>
							</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a class="page-link"
								href="<c:url value="dashboard?number=${i - 1}&size=${pagination.size}&name=${name}&property=${sort.property}&order=${sort.order}"/>">${i}</a>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>

				<c:if test="${pagination.currentPage lt pagination.numbersOfPages}">
					<li class="page-item"><a class="page-link"
						href="<c:url value="dashboard?number=${pagination.currentPage + 1}&size=${pagination.size}&name=${name}&property=${sort.property}&order=${sort.order}"/>"><spring:message code="dashboard.next"/></a>
					</li>
				</c:if>
			</ul>

			<div class="btn-group btn-group-sm pull-right" role="group">
				<a class="page-link"
					href="dashboard?number=${1}&size=${10}&name=${name}&property=${property}&order=${order}">
					<button type="button" class="btn btn-default">10</button>
				</a> <a class="page-link"
					href="dashboard?number=${1}&size=${50}&name=${name}&property=${property}&order=${order}">
					<button type="button" class="btn btn-default">50</button>
				</a> <a class="page-link"
					href="dashboard?number=${1}&size=${100}&name=${name}&property=${property}&order=${order}">
					<button type="button" class="btn btn-default">100</button>
				</a>
			</div>
		</div>
	</footer>
	<script src="<c:url value="/resources/js/jquery.min.js"/>"></script>
	<script src="<c:url value="/resources/js/bootstrap.min.js"/>"></script>
	<script src="<c:url value="/resources/js/dashboard.js"/>"></script>
</body>
</body>
</html>