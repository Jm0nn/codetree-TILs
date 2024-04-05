import java.io.*;
import java.util.*;

public class Main {
	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		StringBuilder sb = new StringBuilder();

		StringTokenizer st = new StringTokenizer(br.readLine());
		int N = Integer.parseInt(st.nextToken());
		int M = Integer.parseInt(st.nextToken());
		int P = Integer.parseInt(st.nextToken());
		int C = Integer.parseInt(st.nextToken());
		int D = Integer.parseInt(st.nextToken());

		st = new StringTokenizer(br.readLine());
		int Rr = Integer.parseInt(st.nextToken());
		int Rc = Integer.parseInt(st.nextToken());

		int[][] santas = new int[P + 1][2];
		int[] points = new int[P + 1];
		boolean[] isDead = new boolean[P + 1];
		int[] stun = new int[P + 1];

		int[][] map = new int[N + 1][N + 1];

		for (int i = 0; i < P; ++i) {
			st = new StringTokenizer(br.readLine());
			int pn = Integer.parseInt(st.nextToken());
			int r = Integer.parseInt(st.nextToken());
			int c = Integer.parseInt(st.nextToken());

			santas[pn][0] = r;
			santas[pn][1] = c;

			map[r][c] = pn;
		}

		final int[][] deltas = {{-1, 0}, {-1, 1}, {0, 1}, {1, 1}, {1, 0}, {1, -1}, {0, -1}, {-1, -1}};
		final int INF = Integer.MAX_VALUE;

		while (M-- > 0) {
			int minDist = INF;
			int minSanta = 0;

			for (int i = 1; i <= P; ++i) {
				if (isDead[i]) {
					continue;
				}

				int dist = getDist(Rr, Rc, santas[i][0], santas[i][1]);
				if (dist < minDist) {
					minDist = dist;
					minSanta = i;
				} else if (dist == minDist) {
					if (santas[minSanta][0] < santas[i][0]) {
						minSanta = i;
					} else if (santas[minSanta][0] == santas[i][0]) {
						if (santas[minSanta][1] < santas[i][1]) {
							minSanta = i;
						}
					}
				}
			}

			minDist = INF;
			int dir = 0;

			for (int d = 0; d < 8; ++d) {
				int nr = Rr + deltas[d][0];
				int nc = Rc + deltas[d][1];

				if (0 >= nr || nr > N || 0 >= nc || nc > N) {
					continue;
				}

				int dist = getDist(nr, nc, santas[minSanta][0], santas[minSanta][1]);

				if (dist < minDist) {
					minDist = dist;
					dir = d;
				}
			}

			Rr += deltas[dir][0];
			Rc += deltas[dir][1];

			if (map[Rr][Rc] > 0) {
				int santa = map[Rr][Rc];

				points[santa] += C;

				map[Rr][Rc] = 0;

				stun[santa] = 2;

				int r = santas[santa][0] += C * deltas[dir][0];
				int c = santas[santa][1] += C * deltas[dir][1];

				if (0 >= r || r > N || 0 >= c || c > N) {
					isDead[santa] = true;
				} else {
					int state = map[r][c];
					map[r][c] = santa;

					while (state > 0) {
						santa = state;

						r = santas[santa][0] += deltas[dir][0];
						c = santas[santa][1] += deltas[dir][1];

						if (0 >= r || r > N || 0 >= c || c > N) {
							isDead[santa] = true;
							break;
						}

						state = map[r][c];
						map[r][c] = santa;
					}
				}
			}

			for (int i = 1; i <= P; ++i) {
				if (isDead[i]) {
					continue;
				} else if (stun[i] > 0) {
					--stun[i];
					continue;
				}

				int r = santas[i][0];
				int c = santas[i][1];

				int dist = getDist(Rr, Rc, r, c);
				minDist = INF;
				dir = -1;

				for (int d = 0; d < 8; d += 2) {
					int nr = r + deltas[d][0];
					int nc = c + deltas[d][1];

					if (0 >= nr || nr > N || 0 >= nc || nc > N) {
						continue;
					} else if (map[nr][nc] > 0) {
						continue;
					}

					int nDist = getDist(Rr, Rc, nr, nc);

					if (dist > nDist) {
						if (minDist > nDist) {
							minDist = nDist;
							dir = d;
						}
					}
				}

				if (dir < 0) {
					continue;
				}

				int santa = i;
				map[r][c] = 0;

				santas[santa][0] = r += deltas[dir][0];
				santas[santa][1] = c += deltas[dir][1];

				map[r][c] = santa;

				if (Rr != r || Rc != c) {
					continue;
				}

				dir = (dir + 4) % 8;

				points[santa] += D;

				map[r][c] = 0;

				stun[santa] = 1;

				r = santas[santa][0] += D * deltas[dir][0];
				c = santas[santa][1] += D * deltas[dir][1];

				if (0 >= r || r > N || 0 >= c || c > N) {
					isDead[santa] = true;
				} else {
					int state = map[r][c];
					map[r][c] = santa;

					while (state > 0) {
						santa = state;

						r = santas[santa][0] += deltas[dir][0];
						c = santas[santa][1] += deltas[dir][1];

						if (0 >= r || r > N || 0 >= c || c > N) {
							isDead[santa] = true;
							break;
						}

						state = map[r][c];
						map[r][c] = santa;
					}
				}
			}

			int count = 0;
			for (int i = 1; i <= P; ++i) {
				if (!isDead[i]) {
					++points[i];
					++count;
				}
			}

			if (count == 0) {
				break;
			}
		}

		for (int i = 1; i <= P; ++i) {
			sb.append(points[i]).append(' ');
		}

		System.out.println(sb);
	}

	static int getDist(int r1, int c1, int r2, int c2) {
		return (r1 - r2) * (r1 - r2) + (c1 - c2) * (c1 - c2);
	}
}