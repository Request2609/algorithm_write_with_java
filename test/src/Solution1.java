public class Solution1 {
    public static void merge(int[] nums1, int m, int[] nums2, int n) {
        int i = 0;
        int j = 0;
        while (j<n&&i<m) {
            System.out.println(i);
            if (nums2[j] <= nums1[i]) {
                System.out.println(i+"   "+j);
                move(i, nums1, m);
                nums1[i] = nums2[j];
                m++;
                j++ ;
            }
            i++ ;
        }
        while(j<nums2.length) {
            nums1[i] = nums2[j] ;
            j++ ;
            i++ ;
        }
    }

    public static void move(int i, int nums[], int m) {
        for (int j = m; j > i; j--) {

            nums[j] = nums[j - 1];
        }
    }

    public static void main(String[] args) {
        int[] nums = {1, 2, 3, 0, 0, 0, 0, 0};
        int[] nums1 = {2, 2, 2};
        merge(nums, 3, nums1, 3);
        System.out.println("结果:");
        for (int i = 0; i <nums.length; i++) {
            System.out.println(nums[i]);
        }
    }
}