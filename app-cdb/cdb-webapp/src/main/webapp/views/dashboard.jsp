<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="fr.excilys.cdb.api.dto.Computer"%>
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
<link href="css/bootstrap.min.css" rel="stylesheet" media="screen">
<link href="css/font-awesome.css" rel="stylesheet" media="screen">
<link href="css/main.css" rel="stylesheet" media="screen">
</head>
<body>
	<header class="navbar navbar-inverse navbar-fixed-top">
		<div class="container">
			<a class="navbar-brand" href="dashboard.html"> Application -
				Computer Database </a>
		</div>
	</header>

	<section id="main">
		<div class="container">
			<h1 id="homeTitle">
				<c:out value="${fn:length(computers)}"></c:out>
				Computers found
			</h1>
			<div id="actions" class="form-horizontal">
				<div class="pull-left">
					<form id="searchForm" action="dashboard" method="GET"
						class="form-inline">

						<input type="search" id="searchbox" name="search"
							class="form-control" placeholder="Search name" /> <input
							type="submit" id="searchsubmit" value="Filter by name"
							class="btn btn-primary" />
					</form>
				</div>

			</div>
			<div class="col-md-6 select-outline">
				<form id="sortForm" action="dashboard" method="GET">
					<div class="col-md-4">
						<select class="form-control" name="sort">
							<option value="" disabled selected>Order By</option>
							<option value='computer'
								<c:if test="${sort == 'computer'}">selected="selected"</c:if>>Computer
								Name</option>
							<option value='company'
								<c:if test="${sort == 'company'}">selected="selected"</c:if>>Company
								Name</option>
						</select>
					</div>
					<div class="col-md-4">
						<select class="form-control" name="by">
							<option value="" disabled selected>TYPE</option>
							<option value="ASC"
								<c:if test="${by == 'ASC'}">selected="selected"</c:if>>ASC</option>
							<option value="DESC"
								<c:if test="${by == 'DESC'}">selected="selected"</c:if>>DESC</option>
						</select>
					</div>
					<input type="submit" id="searchsubmit" value="Submit"
						class="btn btn-primary" />
				</form>
			</div>
			<div class="pull-right">
				<a class="btn btn-success" id="addComputer" href="addComputer">Add
					Computer</a> <a class="btn btn-default" id="editComputer" href="#"
					onclick="$.fn.toggleEditMode();">Edit</a>
			</div>

		</div>
		<form id="deleteForm" action="dashboard" method="POST">
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
						<th>Computer name</th>
						<th>Introduced date</th>
						<!-- Table header for Discontinued Date -->
						<th>Discontinued date</th>
						<!-- Table header for Company -->
						<th>Company</th>

					</tr>
				</thead>
				<!-- Browse attribute computers -->
				<tbody id="results">
					<c:forEach var="item" items="${computers}">
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
				<c:if test="${currentPage != 1}">
					<li class="page-item"><a class="page-link"
						href="dashboard?page=${currentPage - 1}&size=${size}&search=${search}&sort=${sort}&by=${by}">Previous</a>
					</li>
				</c:if>
				<c:forEach begin="${startPage}" end="${endPage}" var="i">
					<c:choose>
						<c:when test="${currentPage eq i}">
							<li class="page-item active"><a class="page-link"> ${i}
									<span class="sr-only">(current)</span>
							</a></li>
						</c:when>
						<c:otherwise>
							<li class="page-item"><a class="page-link"
								href="dashboard?page=${i}&size=${size}&search=${search}&sort=${sort}&by=${by}">${i}</a>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>

				<c:if test="${currentPage lt totalOfPages}">
					<li class="page-item"><a class="page-link"
						href="dashboard?page=${currentPage +1}&size=${size}&search=${search}&sort=${sort}&by=${by}">Next</a>
					</li>
				</c:if>
			</ul>

			<div class="btn-group btn-group-sm pull-right" role="group">
				<a class="page-link"
					href="dashboard?page=${1}&size=${10}&search=${search}&sort=${sort}&by=${by}">
					<button type="button" class="btn btn-default">10</button>
				</a> <a class="page-link"
					href="dashboard?page=${1}&size=${50}&search=${search}&sort=${sort}&by=${by}">
					<button type="button" class="btn btn-default">50</button>
				</a> <a class="page-link"
					href="dashboard?page=${1}&size=${100}&search=${search}&sort=${sort}&by=${by}">
					<button type="button" class="btn btn-default">100</button>
				</a>
			</div>
		</div>
	</footer>
	<script src="js/jquery.min.js"></script>
	<script src="js/bootstrap.min.js"></script>
	<script src="js/dashboard.js"></script>
</body>
</body>
</html>