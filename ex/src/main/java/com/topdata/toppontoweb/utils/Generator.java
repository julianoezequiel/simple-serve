package com.topdata.toppontoweb.utils;

/**
 *
 * @author juliano.ezequiel
 */
public class Generator {

    public static String pis() {

        Integer n = 9;
        Integer n1 = 1;//randomiza(1);
        Integer n2 = 2;//randomiza(2);
        Integer n3 = randomizaMaiorQueZero(5);
        Integer n4 = randomiza(n);
        Integer n5 = randomiza(n);
        Integer n6 = randomiza(n);
        Integer n7 = randomiza(n);
        Integer n8 = randomiza(n);
        Integer n9 = randomiza(n);
        Integer n10 = randomiza(n);
        //+ n10 * 4
        Integer somatorio = (n1 * 3) + (n2 * 2) + (n3 * 9) + (n4 * 8) + (n5 * 7) + (n6 * 6) + (n7 * 5) + (n8 * 4) + (n9 * 3) + (n10 * 2);
        Integer resto = (somatorio % 11);
        Integer resultado = 0;
        if (resto > 1) {
            resultado = 11 - resto;
        }

//        Integer numeroPuro = n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + n10 + resultado;
        return n1.toString() + n2.toString() + n3.toString() + n4.toString() + n5.toString() + n6.toString() + n7.toString() + n8.toString() + n9.toString() + n10.toString() + '-' + resultado.toString();
    }

    public static String cpf() {
        Integer n = 9;
        Integer n1 = randomiza(n);
        Integer n2 = randomiza(n);
        Integer n3 = randomiza(n);
        Integer n4 = randomiza(n);
        Integer n5 = randomiza(n);
        Integer n6 = randomiza(n);
        Integer n7 = randomiza(n);
        Integer n8 = randomiza(n);
        Integer n9 = randomiza(n);
        Integer somatorio1 = n1 * 10 + n2 * 9 + n3 * 8 + n4 * 7 + n5 * 6 + n6 * 5 + n7 * 4 + n8 * 3 + n9 * 2;
        Integer digito1 = 11 - (somatorio1 % 11);

        if ((somatorio1 % 11) < 2) {
            digito1 = 0;
        }

        Integer somatorio2 = n1 * 11 + n2 * 10 + n3 * 9 + n4 * 8 + n5 * 7 + n6 * 6 + n7 * 5 + n8 * 4 + n9 * 3 + digito1 * 2;
        Integer digito2 = 11 - (somatorio2 % 11);

        if ((somatorio2 % 11) < 2) {
            digito2 = 0;
        }

//            numeroPuro = '' + n1 + n2 + n3 + n4 + n5 + n6 + n7 + n8 + n9 + digito1 + digito2;
        return n1.toString() + n2.toString() + n3.toString() + n4.toString() + n5.toString() + n6.toString() + n7.toString() + n8.toString() + n9.toString() + digito1.toString() + digito2.toString();
    }

    public static String cnpj() {
        Integer n = 9;
        Integer d = randomiza(n);
        Integer e = randomiza(n);
        Integer b = randomiza(n);
        Integer c = randomiza(n);
        Integer f = randomiza(n);
        Integer g = randomiza(n);
        Integer h = randomiza(n);
        Integer i = randomiza(n);
        Integer j = 0;
        Integer k = 0;
        Integer l = 0;
        Integer m = 1;
        Long d1 = m.longValue() * 2 + l * 3 + k * 4 + j * 5 + i * 6 + h * 7 + g * 8 + f * 9 + c * 2 + b * 3 + e * 4 + d * 5;
        d1 = 11 - Math.round(d1 - (Math.floor(d1 / 11) * 11));
        d1 = (d1 >= 10) ? 0 : d1;
        Long d2 = d1 * 2 + m * 3 + l * 4 + k * 5 + j * 6 + i * 7 + h * 8 + g * 9 + f * 2 + c * 3 + b * 4 + e * 5 + d * 6;
        d2 = 11 - Math.round(d2 - (Math.floor(d2 / 11) * 11));
        d2 = (d2 >= 10) ? 0 : d2;
//            numeroPuro = '' + d + e + b + c + f + g + h + i + j + k + l + m + d1 + d2;
        return d.toString() + e.toString() + b.toString() + c.toString() + f.toString() + g.toString() + h.toString() + i.toString() + j.toString() + k.toString() + l.toString() + m.toString() + d1.toString() + d2.toString();

    }

    private static Integer randomiza(Integer n) {
        Long ranNum = Math.round(Math.random() * n);
        return ranNum.intValue();
    }

    private static Integer randomizaMaiorQueZero(Integer n) {
        Long num = Math.round(Math.random() * n);

        if (num == 0) {
            num = 1l;
        }

        return num.intValue();
    }
}
