import java.util.*;

class Jembatan{
	//Data type untuk menyimpan tuple pair jembatan
	public static Scanner reader = new Scanner(System.in);
	private int from;
	private int to;
	
	public void inpFromTo(){
		//Method pengambil input dari user
		from = reader.nextInt();
		to = reader.nextInt();
	}
	
	//getter & setter
	public int getFrom(){
		
		return from;
	}
	
	public int getTo(){
		return to;
	}
	
	public void setTo(int a){
		to = a;
	}
	
	public void setFrom(int a){
		from = a;
	}
	
	//Print jembatan
	public void test(){
		System.out.print("(");
		System.out.print(from);
		System.out.print(",");
		System.out.print(to);
		System.out.print(")");
	}
}

class SetOfJembatan{
	//Penyimpan himpunan jembatan agar tidak menyimpan duplikat atau self loop
	private int size;
	private int Neff;
	private Jembatan[] arr;
	
	//inisialisasi dan konstruktor
	public void init(int a){
		size = a;
		Neff = -1;
		arr = new Jembatan[size];
	}
	//pengecek kesamaan
	public boolean equals(Jembatan a, Jembatan b){
		return a.getFrom() == b.getFrom() && a.getTo() == b.getTo();
	}
	//fungsi search dalam set
	public boolean search(Jembatan a){
		int i = 0;
		while (i < Neff && !equals(arr[i], a)){
			i = i+1;
		}
		if (equals(arr[i],a)){
			return true;
		}else{
			return false;
		}
	}
	//tes kekosongan array
	public boolean isEmpty(){
		return Neff == -1;
	}
	//getter
	public Jembatan getJem(int a){
		return arr[a];
	}
	
	//validasi jembatan dan input
	public void add(Jembatan a){
		if (a.getFrom() != a.getTo()){
			if (Neff == -1){
				Neff += 1;
				arr[Neff] = a;
			}else{
				if (!search(a)){
					Neff +=1;
					arr[Neff] = a;
				}
			}
		}
	}
	
	//penghapusan elemen dari set 
	public void remove(int idx){
		if (idx == Neff){
			Neff -= 1;
		}else if (idx == 0){
			for (int i = 0; i < Neff; i++){
				arr[i] = arr[i+1];
			}
			Neff -= 1;
		}else{
			for (int i = idx; i < Neff; i++){
				arr[i] = arr[i+1];
			}
			Neff -= 1;
		}
	}
	
	//penghapusan elemen yang bersifat 2 arah
	public void removedup(int ftemp, int ttemp){
		for (int i = 0; i <= Neff; i++){
			if (arr[i].getFrom() == ttemp && arr[i].getTo() == ftemp){
				remove(i);
			}
		}
	}
	
	//getter Neff
	public int getNeff(){
		return Neff;
	}
	
}

class LonelyIsland{
	//kelas decrease dan conquer utama
	public static Scanner reader = new Scanner(System.in);
	private SetOfJembatan setOfJ = new SetOfJembatan();
	private SetOfJembatan setOfJGen = new SetOfJembatan();
	private int NumOfIsland;
	private int NumOfBridge;
	private int beginning;
	private static int cyclicpivot = -1;
	private List<Integer> steps = new ArrayList<Integer>();
	private boolean[] IslandVisit;
	private List<Integer> solns = new ArrayList<Integer>();
	
	//inisialisasi
	public void inpIsland(){
		NumOfIsland = reader.nextInt();
		NumOfBridge = reader.nextInt(); 
		beginning = reader.nextInt();
		setOfJ.init(NumOfBridge);
		setOfJGen.init(NumOfBridge);
		IslandVisit = new boolean[NumOfIsland+1];
		for (int i = 0; i <= NumOfIsland; i++){
			IslandVisit[i] = false;
		}
		IslandVisit[beginning] = true;
		steps.add(beginning);
	}
	//getter
	public int getstart(){
		return beginning;
	}
	//input bridge
	public void inpBridge(){
		for (int i = 0; i < NumOfBridge; i++){
			Jembatan a = new Jembatan();
			a.inpFromTo();
			setOfJ.add(a);
			setOfJGen.add(a);
		}
		setOfJ.getNeff();
		reader.close();
	}
	
	//penglihatan daftar jembatan
	public void printBridge(int a){
		System.out.println("Available moves: ");
		int n = setOfJ.getNeff();
		for (int i = 0; i <= n; i++){
			if (setOfJ.getJem(i).getFrom() == a){
				setOfJ.getJem(i).test();
			}
		}
		System.out.println("");
	}
	
	//pemastian apakah dari nilai a ada jembatan yang keluar dari a jika tidak mengembalikan nilai -1
	public int pathConfirm(SetOfJembatan b, int a){
		int i = 0;
		int n = b.getNeff();
		while (b.getJem(i).getFrom() != a && i < n){
			i += 1;
		}
		if ((b.getJem(i).getFrom() == a) && n != -1){
			return i;
		}else{
			return -1;
		}
	}
	//mengecek apakah set kosong
	public boolean testEmpty(){
		if (setOfJ.isEmpty()){
			return true;
		}else{
			return false;
		}
	}
	//algoritma decrease and conquer yang mengecek apakah ada path dari pos jika tidak maka prev menyimpan jejak mundur dan setelah itu akan dipanggil lagi DandC yang akan mengembalikan jejak mundur agar menjadi rekursif
	public int DandC(int prev, int pos, boolean bt){
		if (pathConfirm(setOfJ,pos) == -1){
			if (steps.size() > 0){
				if (solns.lastIndexOf(steps.get(steps.size()-1)) != -1){
					System.out.println("Path enum: ");
					for (int i = 0; i < steps.size(); i++){
						if (i == steps.size()-1){
							System.out.println(steps.get(i));
						}else{
							System.out.print(steps.get(i));
							System.out.print(",");
						}
					}
					System.out.println("");
				}
					steps.remove(steps.size()-1);
			}
			return prev;
		}else{
			int q = prev;
			int idx = pathConfirm(setOfJ,pos);
			int j = idx;
			int temp = setOfJ.getJem(idx).getTo();
			int from = setOfJ.getJem(idx).getFrom();
			boolean cyclic = false;
			printBridge(from);
			setOfJ.remove(idx);
			setOfJ.removedup(from,temp);
			System.out.print("Removing available move (");
			System.out.print(from);
			System.out.print(",");
			System.out.print(temp);
			System.out.print(") and similar moves (");
			System.out.print(temp);
			System.out.print(",");
			System.out.print(from);
			System.out.print(")");
			System.out.println("");
			steps.add(temp);
			if (IslandVisit[temp]){
				cyclic = true;
				System.out.print("Switch didn't happen because ");
				System.out.print(temp);
				System.out.println(" has already been visited");
				System.out.println("");
				boolean flag = false;
				if ((bt && solns.lastIndexOf(temp) == -1 && temp == cyclicpivot)){
					solns.add(pos);
					flag = true;
				}
				if (solns.lastIndexOf(steps.get(steps.size()-1)) != -1){
					
					System.out.println("Path enum: ");
					for (int i = 0; i < steps.size(); i++){
						if (i == steps.size()-1){
							System.out.println(steps.get(i));
						}else{
							System.out.print(steps.get(i));
							System.out.print(",");
						}
					}
					System.out.println("");
				}
				if (pathConfirm(setOfJ, temp) == -1){
					steps.remove(steps.size()-1);
				}
				cyclicpivot = temp;
					
			}
			if (!cyclic && pathConfirm(setOfJ,temp) == -1){
				solns.add(temp);
				for (int i = 0; i <= setOfJ.getNeff(); i++){
					if (setOfJ.getJem(i).getFrom() == from){
						if (IslandVisit[setOfJ.getJem(i).getTo()]){
							setOfJ.remove(i);
						}
					}
				}
			}
			if (!IslandVisit[temp]){
				int rec = pos;
				prev = from;
				pos = temp;
				IslandVisit[temp] = true;
				int check = pathConfirm(setOfJ,temp);
				if (check != -1){
					j = check;
					while (IslandVisit[setOfJ.getJem(check).getTo()] && check <= setOfJ.getNeff()){
						check += 1;
					}
					if (setOfJ.getJem(check).getFrom() != setOfJ.getJem(j).getFrom()){
						check = j;
					}
					int checkcyclic = setOfJ.getJem(check).getTo();
					if (!bt && IslandVisit[checkcyclic]){
						solns.add(temp);
					}
				}
				System.out.print("Switch from: ");
				System.out.print(rec);
				System.out.print(" to ");
				System.out.println(pos);
				System.out.print("using: (");
				System.out.print(from);
				System.out.print(",");
				System.out.print(pos);
				System.out.print(") bridge");
				System.out.println("");
				System.out.println("");
			}
			int t = DandC(prev,pos,bt);
			
			bt = true;
			return DandC(q,t,bt);
		}
		
	}
	//print solusi dan isi solns
	public void printsol(){
		Collections.sort(solns);
		System.out.println("");
		System.out.println("Stuck in: ");
		for (int i = 0; i < solns.size(); i++){
			System.out.println(solns.get(i));
		}
		if (solns.isEmpty()){
			System.out.println(beginning);
		}
		
	}
}

public class main implements Runnable {
	//main driver
	//thread agar tidak stackoverflow
     public static void main(String[] args) {
         new Thread(null, new main(), "", 1<<26).start();
     }
	//driver function
     public void run() {
		LonelyIsland t = new LonelyIsland();
		System.out.println("INPUT: ");
		t.inpIsland();
		t.inpBridge();
		System.out.println("");
		double startTime = System.nanoTime();
		if (!t.testEmpty()){
			int x = t.DandC(t.getstart(), t.getstart(), false);
		}else{
			System.out.println("No bridge detected stuck at ");
			System.out.println(t.getstart());
		}
		double endTime   = System.nanoTime();
		t.printsol();
		System.out.println("Total time: ");
		double totalTime = (endTime - startTime)*Math.pow(10,-9);
		System.out.println(totalTime);
     }

}