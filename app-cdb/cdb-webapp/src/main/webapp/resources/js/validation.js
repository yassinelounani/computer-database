$('#discontinued').change(function (){checkDate()})
$('#introduced').change(function (){checkDate()})
$('#ByName').change(function (){byName()})

function checkDate(){
	 if ($('#introduced').val()!= "" && $('#discontinued').val()!= ""){
		 if (moment($('#discontinued').val()).isBefore($('#introduced').val())){
			 $('#submit').attr('disabled',true)
			 $('#spanIntro').html("The discontinued date must be before the introduced date")
		 } else {
		    $('#submit').attr('disabled',false)
		    $('#spanIntro').html("")
		 } 
	 } else {
		 $('#submit').attr('disabled',false)
	     $('#spanIntro').html("")
	 }
};


$(document).ready(function(){
	$("form").validate({
	     	messages: {
	             name: "Please specify the name of computer",
	             required: true
	           },
	        showErrors: function(errorMap, errorList) {
	                if(errorList.length) {
	                    $("#spanName").html(errorList[0].message);
	                }
	            }
	        });
});

function SortByName(a, b){
	  var aName = a.name.toLowerCase();
	  var bName = b.name.toLowerCase(); 
	  return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
}

function SortByCompany(a, b){
	  var aName = a.nameCompany.toLowerCase();
	  var bName = b.nameCompany.toLowerCase(); 
	  return ((aName < bName) ? -1 : ((aName > bName) ? 1 : 0));
}
function byName() {
	if ($('#ByName').val() == "1") {
		computers.sort(SortByName);
	} else {
		computers.sort(SortByCompany);
	}
}