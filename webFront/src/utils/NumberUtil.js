
function isNumber(number){
	if(typeof number === 'number'){
		return true;
	}
	if(parseInt(number) !== NaN){
		return true;
	}
	return false;
}

function isInteger(){
	return typeof obj === 'number' && obj%1 === 0;
}

export default {
	isNumber,
	isInteger,
}