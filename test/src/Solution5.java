public class Solution5 {
    public static int removeElement(int[] nums, int val) {
        int len = nums.length ;
        int count = len ;
        int k = 1;
        int index = 0;
        for(int i=0; i<len&&i+k<len; i++) {
            int flag = 0 ;
            while(i+k<len&&nums[i+k] == val){
                flag = 1 ;
                index = i+1 ;
                k++ ;
                count -- ;
            }
            if(flag == 0) {
                k++ ;
            }
            if(i+k >= len) {
                break ;
            }
            nums[index] = nums[i+k] ;
            i = index ;
        }
        System.out.println("----------------------------------------");
        for(int i=0; i<len; i++) {
            System.out.println(nums[i]);
        }
        System.out.println("----------------------------------------");
        return count ;
    }

    public static void main(String[] args) {
        int []nums = {1,2,3,2,4,5,2} ;
        removeElement(nums, 2) ;
    }
}
