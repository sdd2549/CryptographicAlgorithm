import java.util.Scanner;

public class ToyCipher{
    //마스터키와 S_Box, P_Box에 대한 테이블 그리고 16진수로 표현하기 위한 배열
    final static int[] MasterKey = {4, 4, 5, 5, 4, 14, 4, 5};
    final static int[] S_Box = {14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7};
    final static int[] P_Box = {1, 5, 9, 13, 2, 6, 10, 14, 3, 7, 11, 15, 4, 8, 12, 16};
    final static String Hex = "0123456789ABCDEF";
    
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);

        System.out.print("input : 0x");

        String s = sc.next();

        int[] input = new int[4];

        for(int i=0;i<4;i++){
            input[i] = Hex.indexOf(s.charAt(i));
        }

        int[] res = SPN_Cipher(input);

        System.out.print("result : 0x");

        for(int i=0;i<4;i++){
            System.out.print(Hex.charAt(res[i]));
        }
    }

    public static int[] SPN_Cipher(int[] input){
        int[] plain_text = input;
        
        //Round 1~3
        for(int i=0;i<3;i++){
            plain_text = Xor_withMK(plain_text, i);
            plain_text = S_box(plain_text);
            plain_text = P_box(plain_text);
            System.out.println("");
        }

        //Round 4
        plain_text = Xor_withMK(plain_text, 3);
        plain_text = S_box(plain_text);
        plain_text = Xor_withMK(plain_text, 4);
        System.out.println("");
        return plain_text;
    }

    public static int[] Xor_withMK(int[] in, int round){
        int[] mk = new int[4];
        for(int i=0;i<4;i++){
            mk[i] = MasterKey[round+i];//MasterKey에서 사용할 부분  mk
        }
        //log
        System.out.print("Round" + (round + 1) + " key : 0x");
        for(int i=0;i<4;i++){
            System.out.print(Hex.charAt(mk[i]));
        }
        System.out.println("");
        //input과 mk를 길이가 16인 2진수 배열로 저장
        int[] input_hex = InttoHex(in);
        int[] mk_hex = InttoHex(mk);

        //mk와 input의 Xor연산 실행결과를 input_hex에 저장
        for(int i=0;i<16;i++){
            input_hex[i] = input_hex[i] ^ mk_hex[i];
        }

        //길이가 4인 10진수 배열로 결과 저장
        int res[] = HextoInt(input_hex);
        //log
        System.out.print("result of Xor with MK : 0x");
        for(int i=0;i<4;i++){
            System.out.print(Hex.charAt(res[i]));
        }
        System.out.println("");
        return res;
    }

    public static int[] S_box(int[] plain){
        //16진수의 수를 S_Box테이블에 맞게 Confusion
        for(int i=0;i<4;i++){
            plain[i] = S_Box[plain[i]];
        }
        //log
        System.out.print("S_box : 0x");
        for(int i=0;i<4;i++){
            System.out.print(Hex.charAt(plain[i]));
        }
        System.out.println("");
        return plain;
    }

    public static int[] P_box(int[] plain){
        //들어온 값을 16진수로 변환
        int[] hex = InttoHex(plain);
        int[] tmp = new int[16];

        //P_Box테이블에 맞게 Diffusion
        for(int i=0;i<16;i++){
            tmp[i] = hex[P_Box[i]-1];
        }
        int[] res = new int[4];
        //결과값을 10진수로 변환
        res = HextoInt(tmp);

        //log
        System.out.print("P_box : 0x");
        for(int i=0;i<4;i++){
            System.out.print(Hex.charAt(res[i]));
        }
        System.out.println("");
        return res;
    }
    //10진수로 저장되어 있는 배열을 길이가 16인 2진수 배열로 변환
    public static int[] HextoInt(int[] hex){
        int[] res = new int[4];
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                res[i]*=2;
                res[i]+=hex[4*i+j];
            }
        }
        return res;
    }
    //길이가 16인 2진수 배열을 길이가 4인 10진수로 변환
    public static int[] InttoHex(int[] in){
        int[] hex = new int[16];
        for(int i=0;i<4;i++){
            for(int j=0;j<4;j++){
                hex[4*i+3-j] = in[i]%2;
                in[i]/=2;
            }
        }
        return hex;
    }
}