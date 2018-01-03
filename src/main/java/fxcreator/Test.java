package fxcreator;

/**
 *
 * @author samyn_000
 */
public class Test {

    public static void main(String[] args) {
        long prod = 1;
        long fs[] = new long[1204];
        long fso[] = new long[1024];

        int factorIndex = 0;

        
        for (int i = 2; i < 9; ++i) {
            // find factorial combination
            // write i as product of primes.
            long elf = i;
            for (int j = 0; j < factorIndex; ++j) {
                int o = 0;
                while (elf % fs[j] == 0) {
                    o++;
                    elf /= fs[j];
                }
                if (o > fso[j]) {
                    long diff = o - fso[j];
                    fso[j] += diff;
                    for (int k = 0; k < diff; ++k) {
                        prod *= fs[j];
                    }
                }
                if ( elf == 1){
                    break;
                }
            }
            if ( elf == i){
                // no factor found
                fs[factorIndex] = elf;
                fso[factorIndex++] = 1;
                prod *= elf;
            }
        }
                
        for( int i = 0; i< factorIndex; ++i){
            System.out.println("Factor:" + fs[i] +"," + fso[i]);
        }
        System.out.println("Product is : " + prod);
    }
    
   
}
