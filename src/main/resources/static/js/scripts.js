console.log("checking");
/*alert("I'm active");*/
const toggleSidebar = () => {
	if ($(".sidebar").is(":visible")) {

		$(".sidebar").css("display", "none");
		$(".content").css("margin-left", "0%");
	} else {
		$(".sidebar").css("display", "block");
		$(".content").css("margin-left", "20%");
	}

};


function deletecontact(cid)
{
	swal({
		  title: "Are you sure?",
		  text: "you want to delete this",
		  icon: "warning",
		  buttons: true,
		  dangerMode: true,
		})
		.then((willDelete) => {
		  if (willDelete) {
			  window.location="/user/delete/"+cid;
		   
		   
		  } else {
		    swal("Your contact file is safe!");
		  }
		});

	
	}
	

const search=()=>{
	//console.log();
	let query=$("#search").val();
	if(query=='')
	{
		$(".searchresult").hide();
	}
	else{
		console.log(query);
		let url=`http://localhost:8080/search/${query}`;
		fetch(url)
		.then((response)=>{
			
			return response.json();
		})
		.then((data)=>{
			console.log(data);
			let text=`<div class='list-group'>`;
			
			data.forEach((contact)=>{
				text+=`<a href='/user/${contact.cid}/contact' class='list-group-item list-group-action'>${contact.cname}</a>`
			});
		text+=`</div>`;
			$(".searchresult").html(text);
		$(".searchresult").show();
		
		});
	
	}
};
