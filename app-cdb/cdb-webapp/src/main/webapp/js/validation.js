$('#submit').attr('disabled',true)
$('#discontinued').change(function (){checkDate()})
$('#introduced').change(function (){checkDate()})

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
function checkName() {
	if ($('#computerName').val() != ""){
  		 $('#submit').attr('disabled',false)
  		 $('#spanName').html("")
  	} else {
  		$('#submit').attr('disabled',true)
  		$('#spanName').html("Please specify the name of computer")
  	}
};

