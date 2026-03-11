import java.util.Random;

public class Sistema {
    public static void main(String[] args) {

        int[] energiaContenedores = new int[12];
        int[] multiplos10 = new int[12];
        Random random = new Random();
        for (int i = 0; i < energiaContenedores.length; i++){

            energiaContenedores[i] = random.nextInt(101) + 50;
            if (energiaContenedores[i] % 10 == 0){
                multiplos10[i] = energiaContenedores[i];
            }

        }

        int[][] mapaCarga = new int[3][3];
        int index = 0;
        for (int i = 0; i < 3;i++){

            for (int j = 0;i < 3;i++){

                if(multiplos10 != null){
                    mapaCarga[i][j] = multiplos10[index];
                    index++;
                }
                else
                    mapaCarga[i][j] = -1;
                
            }
        }

        Suministro[] Suministro = new Suministro[9];
        int contador = 0;
        String prioridad;
        for(int i = 0;i < 3;i++){

            for (int j = 0;j < 3;j++ ){

                if(mapaCarga[i][j] != -1){
                    
                    int energia = mapaCarga[i][j];

                    if (energia > 100){

                        prioridad = "Alta";
                    }else{
                        prioridad = "Estandar";
                    }

                    Suministro[contador] = new Suministro("d" + random.nextInt(10),energia,prioridad);
                }else
                    Suministro = null;

                contador++;
            }
        }

        //Imprimir los suministros
        for (Suministro s : Suministro){

            if(s != null){
                System.out.println(s);
            }else{

            }

        }
    }

}
