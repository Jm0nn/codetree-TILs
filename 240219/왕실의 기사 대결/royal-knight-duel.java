import java.io.*;
import java.util.*;

public class Main {

	private static class Knight {

		int r, c; // 기사의 위치
		int h, w; // 기사의 크기
		int k, l; // 초기 체력, 남은 체력

		public Knight(int r, int c, int h, int w, int k) {
			this.r = r;
			this.c = c;
			this.h = h;
			this.w = w;
			this.k = k;
			this.l = k;
		}

	}

	private static final int[][] deltas = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

	private static int L, N, Q; // 체스판 크기, 기사의 수, 명령의 수
	private static int cur;
	private static int[][] board; // 체스판
	private static int[][] knightPos; // 기사의 위치
	private static Knight[] knights; // 기사 정보

	private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	private static StringTokenizer st;

	public static void main(String[] args) throws Exception {
		st = new StringTokenizer(br.readLine());
		L = Integer.parseInt(st.nextToken());
		N = Integer.parseInt(st.nextToken());
		Q = Integer.parseInt(st.nextToken());

		board = new int[L + 1][L + 1];
		knightPos = new int[L + 1][L + 1];
		knights = new Knight[N + 1];

		for (int i = 1; i <= L; ++i) {
			st = new StringTokenizer(br.readLine());
			for (int j = 1; j <= L; ++j) {
				board[i][j] = Integer.parseInt(st.nextToken());
			}
		}

		for (int i = 1; i <= N; ++i) {
			st = new StringTokenizer(br.readLine());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());
			int h = Integer.parseInt(st.nextToken());
			int w = Integer.parseInt(st.nextToken());
			int k = Integer.parseInt(st.nextToken());

			knights[i] = new Knight(r, c, h, w, k);

			for (int x = r; x < r + h; ++x) {
				for (int y = c; y < c + w; ++y) {
					knightPos[x][y] = i;
				}
			}
		}

		for (int k = 0; k < Q; ++k) {
			st = new StringTokenizer(br.readLine());
			int i = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			if (moveChk(i, d)) {
				cur = i;
				move(i, d);
			}
		}

		int dmg = 0;
		for (int i = 1; i <= N; ++i) {
			Knight cur = knights[i];
			if (cur.l > 0) {
				dmg += cur.k - cur.l;
			}
		}
		System.out.println(dmg);
	}

	private static boolean moveChk(int i, int d) {
		if (i == 0) {
			return true;
		}

		Knight knight = knights[i];

		if (knight.l <= 0) {
			return false;
		}

		int[][] arr = getArr(i, d);

		for (int[] a : arr) {
			int r = a[0];
			int c = a[1];

			if (0 >= r || r > L) {
				return false;
			} else if (0 >= c || c > L) {
				return false;
			} else if (board[r][c] == 2) {
				return false;
			}
		}

		boolean[] flag = new boolean[N + 1];
		flag[i] = true;
		for (int[] a : arr) {
			int r = a[0];
			int c = a[1];
			int num = knightPos[r][c];

			if (flag[num]) {
				continue;
			}

			flag[num] = true;

			if (!moveChk(num, d)) {
				return false;
			}
		}

		return true;
	}

	private static void move(int i, int d) {
		if (i == 0) {
			return;
		}

		int[][] arr = getArr(i, d);

		for (int[] a : arr) {
			int r = a[0];
			int c = a[1];
			int num = knightPos[r][c];

			move(num, d);
		}

		Knight knight = knights[i];

		int r = knight.r;
		int c = knight.c;

		for (int j = r; j < r + knight.h; ++j) {
			for (int k = c; k < c + knight.w; ++k) {
				knightPos[j][k] = 0;
			}
		}

		r += deltas[d][0];
		c += deltas[d][1];

		for (int j = r; j < r + knight.h; ++j) {
			for (int k = c; k < c + knight.w; ++k) {
				knightPos[j][k] = 0;
			}
		}

		knight.r = r;
		knight.c = c;

		for (int j = r; j < r + knight.h; ++j) {
			for (int k = c; k < c + knight.w; ++k) {
				knightPos[j][k] = i;
			}
		}

		if (cur == i) {
			return;
		}

		boolean dead = false;

		for (int j = r; j < r + knight.h; ++j) {
			for (int k = c; k < c + knight.w; ++k) {
				if (board[j][k] == 1) {
					--knight.l;

					if (knight.l <= 0) {
						dead = true;
					}
				}
			}
		}

		if (dead) {
			for (int j = r; j < r + knight.h; ++j) {
				for (int k = c; k < c + knight.w; ++k) {
					knightPos[j][k] = 0;
				}
			}
		}
	}

	private static int[][] getArr(int i, int d) {
		Knight knight = knights[i];

		int l;
		int[][] arr;
		if (d == 0) {
			l = knight.w;
			arr = new int[l][2];
			for (int j = 0; j < l; ++j) {
				arr[j][0] = knight.r - 1;
				arr[j][1] = knight.c + j;
			}
		} else if (d == 1) {
			l = knight.h;
			arr = new int[l][2];
			for (int j = 0; j < l; ++j) {
				arr[j][0] = knight.r + j;
				arr[j][1] = knight.c + knight.w;
			}
		} else if (d == 2) {
			l = knight.w;
			arr = new int[l][2];
			for (int j = 0; j < l; ++j) {
				arr[j][0] = knight.r + knight.h;
				arr[j][1] = knight.c + j;
			}
		} else {
			l = knight.h;
			arr = new int[l][2];
			for (int j = 0; j < l; ++j) {
				arr[j][0] = knight.r + j;
				arr[j][1] = knight.c - 1;
			}
		}

		return arr;
	}

}