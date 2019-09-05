
function isNumber(number){
	if(typeof number === 'number'){
		return true;
	}
	if(parseInt(number) !== NaN){
		return true;
	}
	return false;
}

export default {
	isNumber,
}