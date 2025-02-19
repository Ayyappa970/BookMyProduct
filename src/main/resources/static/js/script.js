//User Registration Validation
$(function(){
	var $userRegister= $("#userRegister");
	$userRegister.validate({
		rules:{
			userName:{
				required:true,
				lettersonly:true
				},
			mobileNumber:{
				required:true,
				numbersonly:true,
				noSpace:true,
				minlength:10,
				maxlength:12
				},
			email:{
				required:true,
				email:true,
				noSpace:true
				},
			address:{
				required:true,
				all:true
				},
			city:{
				required:true,
				noSpace:true,
				lettersonly:true
				},
			state:{
				required:true,
				noSpace:true,
				lettersonly:true
				},
			pincode:{
				required:true,
				noSpace:true,
				numbersonly:true
				},
			password:{
				required:true,
				noSpace:true,
				minlength:8,
				maxlength:10
				},
			cPassword:{
				required:true,
				noSpace:true,
				minlength:8,
				maxlength:10,
				equalTo:"#pass"
				},
			file:{
				required:true,
				}
		},
		messages:{
			userName:{
				required:	'full name required',
				lettersonly:"invalid name"
			},  //when error occured .error class will created automaticlly we can give color in css file
			mobileNumber:{
				required:	'mobile number required',
				numbersonly:"numbers only",
				noSpace:"noSpace required",
				minlength:"min 10 numbers needed",
				maxlength:"invalid mobile number"
			},
			email:{
				required:	'email required',
				email:"enter valid email address",
				noSpace:"noSpace required"
			},
			address:{
				required:	'address required',
				all:"invalid"
			},
			city:{
				required:	'city name required',
				noSpace:"noSpace required",
				lettersonly:"invalid name"
			},
			state:{
				required:	'state name required',
				noSpace:"noSpace required",
				lettersonly:"invalid name"
			},
			pincode:{
				required:	'pincode required',
				noSpace:"noSpace required",
				numbersonly:"numbers only"
			},
			password:{
				required:	'password required',
				noSpace:"noSpace required",
				minlength:"min 8 characters needed",
				maxlength:"max reached"
			},
			cPassword:{
				required:	'confirm password required',
				noSpace:"noSpace required",
				minlength:"min 8 characters needed",
				maxlength:"max reached",
				equalTo:"password mismatch"
			},
			file:{
				required:	'image required',
			}
		}
	})
}
)


//Reset Password Validation
$(function(){
	var $resetPassword= $("#resetPassword");
	$resetPassword.validate({
		rules:{
			password:{
				required:true,
				noSpace:true,
				minlength:8,
				maxlength:10
				},
			cPassword:{
				required:true,
				noSpace:true,
				minlength:8,
				maxlength:10,
				equalTo:"#rePass"
				}
			},
			messages:{
				password:{
				required:	'password required',
				noSpace:"noSpace required",
				minlength:"min 8 characters needed",
				maxlength:"max reached"
			},
			cPassword:{
				required:	'confirm password required',
				noSpace:"noSpace required",
				minlength:"min 8 characters needed",
				maxlength:"max reached",
				equalTo:"password mismatch"
			}
			}
		})
	})
	
	//user Password change Validation
$(function(){
	var $userPasswordChange= $("#userPasswordChange");
	$userPasswordChange.validate({
		rules:{
			oldPass:{
				required:true
			},
			newPass:{
				required:true,
				noSpace:true,
				minlength:8,
				maxlength:10
				},
			confirmPass:{
				required:true,
				noSpace:true,
				minlength:8,
				maxlength:10,
				equalTo:"#newPassword"
				}
			},
			messages:{
				oldPass:{
					required:	'old password required'
				},
				newPass:{
				required:	'password required',
				noSpace:"noSpace required",
				minlength:"min 8 characters needed",
				maxlength:"max reached"
			},
			confirmPass:{
				required:	'confirm password required',
				noSpace:"noSpace required",
				minlength:"min 8 characters needed",
				maxlength:"max reached",
				equalTo:"password mismatch"
			}
			}
		})
	})
	
	
	//Shipping details Validation
	$(function(){
	var $ordersvalidation= $("#ordersvalidation");
	$ordersvalidation.validate({
		rules:{
			firstName:{
				required:true,
				lettersonly:true
				},
			lastName:{
				required:true,
				lettersonly:true
				},
			mobileNumber:{
				required:true,
				numbersonly:true,
				noSpace:true,
				minlength:10,
				maxlength:12
				},
			email:{
				required:true,
				email:true,
				noSpace:true
				},
			address:{
				required:true,
				all:true
				},
			city:{
				required:true,
				noSpace:true,
				lettersonly:true
				},
			state:{
				required:true,
				noSpace:true,
				lettersonly:true
				},
			pincode:{
				required:true,
				noSpace:true,
				numbersonly:true
				},
			paymentType:{
				required:true
			}
		},
		messages:{
			firstName:{
				required:	'first name required',
				lettersonly:"invalid name"
			},  
			lastName:{
				required:	'last name required',
				lettersonly:"invalid name"
			},
			mobileNumber:{
				required:	'mobile number required',
				numbersonly:"numbers only",
				noSpace:"noSpace required",
				minlength:"min 10 numbers needed",
				maxlength:"invalid mobile number"
			},
			email:{
				required:	'email required',
				email:"enter valid email address",
				noSpace:"noSpace required"
			},
			address:{
				required:	'address required',
				all:"invalid"
			},
			city:{
				required:	'city name required',
				noSpace:"noSpace required",
				lettersonly:"invalid name"
			},
			state:{
				required:	'state name required',
				noSpace:"noSpace required",
				lettersonly:"invalid name"
			},
			pincode:{
				required:	'pincode required',
				noSpace:"noSpace required",
				numbersonly:"numbers only"
			},
			paymentType:{
				required:	"select payment type"
			}
		}
	})
})
	
//user Login validation
$(function(){
	var $userLogin= $("#userLogin");
	$userLogin.validate({
		rules:{
			username:{
				required:true,
				email:true,
				noSpace:true
				},
			password:{
				required:true,
				noSpace:true,
				minlength:4,
				maxlength:10
				}
		},
		messages:{
			username:{
				required:	'email required',
				email:"enter valid email address",
				noSpace:"noSpace required"
			},
			password:{
				required:	'password required',
				noSpace:"noSpace required",
				minlength:"min 4 characters needed",
				maxlength:"max reached"
			}
		}
	})
})


//add new product validation
$(function(){
	var $addNewProduct= $("#addNewProduct");
	$addNewProduct.validate({
		rules:{
			pName:{
				required:true,
				all:true
				},
			description:{
				required:true,
				all:true,
				minlength:20,
				maxlength:100
				},
			category:{
				required:true
				},
			price:{
				required:true,
				noSpace:true,
				numbersonly:true
				},
			stock:{
				required:true,
				noSpace:true,
				numbersonly:true
				},
			file:{
				required:	true
			}
		},
		messages:{
			pName:{
				required:	'product name required',
				all:"invalid product name"
			},
			description:{
				required:	'description required',
				all:"enter valid description",
				minlength:"min 20 characters needed",
				maxlength:"max reached"
			},
			category:{
				required:	'category required',
			},
			price:{
				required:	'price required',
				noSpace:"noSpace required",
				numbersonly:"numbers only"
			},
			stock:{
				required:	'stock required',
				noSpace:"noSpace required",
				numbersonly:"numbers only"
			},
			file:{
				required:	'image required',
			}
		}
	})
})


//add new category validation
$(function(){
	var $addNewCategory= $("#addNewCategory");
	$addNewCategory.validate({
		rules:{
			name:{
				required:true,
				all:true
				},
			file:{
				required:	true
			}
		},
		messages:{
			name:{
				required:	'category name required',
				all:"invalid category name"
			},
			file:{
				required:	'image required',
			}
		}
	})
})


	
	//for alphabet letters validation
jQuery.validator.addMethod("lettersonly", function(value, element) {
  return this.optional(element) || /^[a-z]+$/i.test(value);
});

 jQuery.validator.addMethod("noSpace", function(value) { 
  return value.indexOf(" ") < 0 && value != ""; 
});

jQuery.validator.addMethod("numbersonly", function(value, element) {
  return this.optional(element) || /^[0-9]+$/i.test(value);
});

jQuery.validator.addMethod("email", function(value, element) {
  return this.optional(element) || /^[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$/i.test(value);
});

jQuery.validator.addMethod("all", function(value, element) {
  return this.optional(element) || /^[a-zA-Z0-9," "]+$/i.test(value);
});