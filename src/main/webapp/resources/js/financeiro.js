function mascara(o, f) {
			v_obj = o;
			v_fun = f;
			setTimeout("execmascara()", 1);
		}

		function execmascara() {
			v_obj.value = v_fun(v_obj.value);
		}

		function valor(v) {
			v = v.replace(/\D/g, "");
			v = v.replace(/[0-9]{15}/, "invÃ¡lido");
			v = v.replace(/(\d{1})(\d{11})$/, "$1.$2"); // coloca ponto antes dos
			// Ãºltimos 11 digitos
			v = v.replace(/(\d{1})(\d{8})$/, "$1.$2"); // coloca ponto antes dos
			// Ãºltimos 8 digitos
			v = v.replace(/(\d{1})(\d{5})$/, "$1.$2"); // coloca ponto antes dos
			// Ãºltimos 5 digitos
			v = v.replace(/(\d{1})(\d{1,2})$/, "$1,$2"); // coloca virgula antes dos
			// Ãºltimos 2 digitos
			return v;
		}