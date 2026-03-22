package main.java;

/**
 * An empirical evaluation of five sorting algorithms.
 */
public class Main {

  /**
   * Should benchmark all five sorting algorithms using randomly
   * generated arrays of size 100, 1_000, 10_000, 100_000, and 1_000_000.
   * The number of significant operations required for each
   * algorithm to sort each array should be reported.
   *
   * Afterwards, use the JMH to benchmark the timings of each algorithm.
   *
   * @param args the command-line arguments
   */
  public static void main(String[] args) {
    // TODO: benchmark the sig ops of all five sorting algorithms
    long seed = 102238593l;
    for (int size: new int[]{100, 1_000, 10_000, 100_000, 1_000_000}){
      int[] benchmark = getBenchData(size, seed);
      reportOps("Bubble Sort", benchmark.clone(), new BubbleSort());
      reportOps("Insertion Sort", benchmark.clone(), new InsertionSort());
      reportOps("Merge Sort", benchmark.clone(), new MergeSort());
      reportOps("Quick Sort", benchmark.clone(), new QuickSort());
      reportOps("Radix Sort", benchmark.clone(), new RadixSort());
    }
  }

  public static void reportOps(String name, int[] data, Sorter algo){
    algo.sort(data);
    System.out.printf("%s,%d,%d\n", name, data.length, algo.ops());
  }
  public static int[] getBenchData(int size, long seed) {
    java.util.Random rng = new java.util.Random(seed);
    int[] data = new int[size];
    for (int i = 0; i < size; i++) {
      data[i] = rng.nextInt(0, Integer.MAX_VALUE);
    }
    return data;
  }

  public abstract static class Sorter {
    protected long sigOps = 0;

    protected static void swap(int[] arr, int a, int b) {
      int tmp = arr[a];
      arr[a] = arr[b];
      arr[b] = tmp;
    }

    public long ops() {
      return this.sigOps;
    }

    public void sort(int[] arr) {
      this.sigOps = 0;
      this.sort(arr, 0, arr.length);
    }

    public abstract void sort(int[] arr, int start, int end);
  }

  public static class BubbleSort extends Sorter {
    public void sort(int[] arr, int start, int end) {
      // TODO: implement bubble sort
      boolean swap;
      for (int i = 0; i < arr.length - 1; i++){
        swap = false;
        for (int j = 0; j < arr.length - i - 1; j++){
          this.sigOps++;
          if(arr[j] > arr[j + 1]){
            swap(arr, j, j + 1);
            swap = true;
          }
        }
        if (!swap){
          return;
        }
      }
    }
  }

  public static class InsertionSort extends Sorter {
    public void sort(int[] arr, int start, int end) {
      // TODO: implement insertion sort
      for (int i = 1; i < arr.length; i++){
        int n = i;
        //this.sigOps++;
        while (n > 0){
          this.sigOps++;
          if (arr[n] < arr[n - 1]){
            swap(arr, n, n - 1);
            n--;
          }
          else{
            break;
          }
        }
      }
    }
  }

  public static class MergeSort extends Sorter {
    public void merge(int[] arr, int start, int mid, int end) {
      // TODO: implement merge sort
      int[] leftArr = new int[mid - start];
      int[] rightArr = new int[end - mid];
      // This for loop puts elements in the left half of the entire array
      for (int i = 0; i < leftArr.length; i++) {
        leftArr[i] = arr[start + i];
      }
      // This for loop puts elements for the right half of the entire array
      for (int i = 0; i < rightArr.length; i++) {
        rightArr[i] = arr[mid + i];
      }
      
      int e = 0;
      int f = 0;
      int g = start;
      
      while (e < leftArr.length && f < rightArr.length){
        this.sigOps++;
        if (leftArr[e] <= rightArr[f]){
          arr[g++] = leftArr[e++];
        }
        else{
          arr[g++] = rightArr[f++];
        }
      }
      while (e < leftArr.length) {
        arr[g++] = leftArr[e++];
      }
      while (f < rightArr.length) {
        arr[g++] = rightArr[f++];
      }
    }
    public void sort(int[] arr, int start, int end) {
      // TODO: implement merge sort
      if (start + 1 >= end){
        return;
      }
      int mid = (start + end) / 2;
      /*int [] start_arr = new int[mid - start];
      int [] end_arr = new int[end - mid];
      for (int i = 0; i < mid - start; i++) {
          start_arr[i] = arr[start + i];
      }
      for (int i = mid; i < end; i++) {
          end_arr[i - mid] = arr[i];
      }*/
      sort(arr, start, mid);
      sort(arr, mid, end);
      merge(arr, start, mid, end);
    }
  }

  public static class QuickSort extends Sorter {
    public int partition(int arr[], int first, int last){
      int pivot = arr[last - 1];
      int before = first - 1;
      //sigOps++;
      for (int i = first; i < last - 1; i++){
        this.sigOps++;
        if (arr[i] <= pivot){
          before++;
          swap(arr, before, i);
        }
      }
      swap(arr, before + 1, last - 1);
      return before + 1;
    }

    public void sort(int[] arr, int start, int end) {
      // TODO: implement quick sort
      if (start < end){
        int segment = partition(arr, start, end);
        sort(arr, start, segment);
        sort(arr, segment + 1, end);
      }
    }
  }

  public static class RadixSort extends Sorter {
    private static int max(int[] arr, int begin, int end){
      if (begin >= end){
        throw new RuntimeException("no maximum for an empty slice");
      }
      if (begin + 1 == end){
        return arr[begin];
      }
      int mid = (begin + end) / 2;
      return Math.max(max(arr, begin, mid), max(arr, mid, end));
    }
    public void sort(int[] arr, int start, int end) {
      // TODO: implement radix sort
      if (start >= end) return;
      int maxVal = max(arr, start, end);
      for (int exp = 1; maxVal / exp > 0; exp *= 10) {
        countingSort(arr, start, end, exp);
      }
    }
    private void countingSort(int[] arr, int start, int end, int exp) {
      int[] output = new int[end - start];
      int[] count = new int[10];
      for (int i = start; i < end; i++) {
        count[(arr[i] / exp) % 10]++;
        this.sigOps++;
      }
      for (int i = 1; i < 10; i++) {
        count[i] += count[i - 1];
        //this.sigOps++; not necessary for declaring element position
      }
      for (int i = end - 1; i >= start; i--) {
        output[count[(arr[i] / exp) % 10] - 1] = arr[i];
        count[(arr[i] / exp) % 10]--;
        this.sigOps++;
      }
      for (int i = start; i < end; i++) {
        arr[i] = output[i - start];
        //this.sigOps++; not necessary for declaring element position
      }
    }
  }
}
