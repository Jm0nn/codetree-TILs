import java.io.*;
import java.util.*;

public class Main {
	static class Player {
		int num; // 플레이어 번호
		int x, y; // 플레이어 위치
		int s; // 플레이어 초기 능력치
		int d; // 플레이어 방향
		int gun; // 총의 공격력

		public Player(int num, int x, int y, int s, int d, int gun) {
			this.num = num;
			this.x = x;
			this.y = y;
			this.s = s;
			this.d = d;
			this.gun = gun;
		}
	}

	static final int[][] deltas = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

	static int n, m, k;
	static int[] points;
	static Player[] players;
	static Player[][] playerPos;
	static PriorityQueue<Integer>[][] guns;

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		StringTokenizer st = new StringTokenizer(br.readLine());
		n = Integer.parseInt(st.nextToken());
		m = Integer.parseInt(st.nextToken());
		k = Integer.parseInt(st.nextToken());

		points = new int[m + 1];
		players = new Player[m + 1];
		playerPos = new Player[n + 1][n + 1];
		guns = new PriorityQueue[n + 1][n + 1];

		for (int i = 1; i <= n; ++i) {
			st = new StringTokenizer(br.readLine());

			for (int j = 1; j <= n; ++j) {
				guns[i][j] = new PriorityQueue<>((o1, o2) -> o2 - o1);
				guns[i][j].offer(Integer.parseInt(st.nextToken()));
			}
		}

		for (int i = 1; i <= m; ++i) {
			st = new StringTokenizer(br.readLine());
			int x = Integer.parseInt(st.nextToken());
			int y = Integer.parseInt(st.nextToken());
			int d = Integer.parseInt(st.nextToken());
			int s = Integer.parseInt(st.nextToken());

			players[i] = new Player(i, x, y, s, d, 0);
			playerPos[x][y] = players[i];
		}

		while (k-- > 0) {
			for (int i = 1; i <= m; ++i) {
				Player p = players[i];
				int x = p.x;
				int y = p.y;
				int d = p.d;

				playerPos[x][y] = null;

				x += deltas[d][0];
				y += deltas[d][1];

				if (0 >= x || x > n || 0 >= y || y > n) {
					d = (d + 2) % 4;
					x += deltas[d][0] * 2;
					y += deltas[d][1] * 2;
					p.d = d;
				}

				p.x = x;
				p.y = y;

				if (playerPos[x][y] == null) {
					playerPos[x][y] = p;

					if (!guns[x][y].isEmpty() && p.gun < guns[x][y].peek()) {
						guns[x][y].offer(p.gun);
						p.gun = guns[x][y].poll();
					}
				} else {
					Player p2 = playerPos[x][y];

					if (p.s + p.gun > p2.s + p2.gun) {
						int point = p.s + p.gun - p2.s - p2.gun;
						points[p.num] += point;

						guns[x][y].offer(p2.gun);
						p2.gun = 0;

						int x2 = p2.x;
						int y2 = p2.y;
						int d2 = p2.d;

						int nx2 = x2 + deltas[d2][0];
						int ny2 = y2 + deltas[d2][1];

						while (0 >= nx2 || nx2 > n || 0 >= ny2 || ny2 > n || playerPos[nx2][ny2] != null) {
							d2 = (d2 + 1) % 4;
							p2.d = d2;
							nx2 = x2 + deltas[d2][0];
							ny2 = y2 + deltas[d2][1];
						}

						p2.x = nx2;
						p2.y = ny2;

						playerPos[nx2][ny2] = p2;

						if (!guns[nx2][ny2].isEmpty()) {
							p2.gun = guns[nx2][ny2].poll();
						}

						playerPos[x][y] = p;

						if (!guns[x][y].isEmpty() && p.gun < guns[x][y].peek()) {
							p.gun = guns[x][y].poll();
						}
					} else if (p.s + p.gun < p2.s + p2.gun) {
						int point = p2.s + p2.gun - p.s - p.gun;
						points[p2.num] += point;

						guns[x][y].offer(p.gun);
						p.gun = 0;

						int x2 = p.x;
						int y2 = p.y;
						int d2 = p.d;

						int nx2 = x2 + deltas[d2][0];
						int ny2 = y2 + deltas[d2][1];

						while (0 >= nx2 || nx2 > n || 0 >= ny2 || ny2 > n || playerPos[nx2][ny2] != null) {
							d2 = (d2 + 1) % 4;
							p.d = d2;
							nx2 = x2 + deltas[d2][0];
							ny2 = y2 + deltas[d2][1];
						}

						p.x = nx2;
						p.y = ny2;

						playerPos[nx2][ny2] = p;

						if (!guns[nx2][ny2].isEmpty()) {
							p.gun = guns[nx2][ny2].poll();
						}

						playerPos[x][y] = p2;

						if (!guns[x][y].isEmpty() && p2.gun < guns[x][y].peek()) {
							p2.gun = guns[x][y].poll();
						}
					} else {
						if (p.s > p2.s) {
							guns[x][y].offer(p2.gun);
							p2.gun = 0;

							int x2 = p2.x;
							int y2 = p2.y;
							int d2 = p2.d;

							int nx2 = x2 + deltas[d2][0];
							int ny2 = y2 + deltas[d2][1];

							while (0 >= nx2 || nx2 > n || 0 >= ny2 || ny2 > n || playerPos[nx2][ny2] != null) {
								d2 = (d2 + 1) % 4;
								p2.d = d2;
								nx2 = x2 + deltas[d2][0];
								ny2 = y2 + deltas[d2][1];
							}

							p2.x = nx2;
							p2.y = ny2;

							playerPos[nx2][ny2] = p2;

							if (!guns[nx2][ny2].isEmpty()) {
								p2.gun = guns[nx2][ny2].poll();
							}

							playerPos[x][y] = p;

							if (!guns[x][y].isEmpty() && p.gun < guns[x][y].peek()) {
								p.gun = guns[x][y].poll();
							}
						} else {
							int point = p2.s + p2.gun - p.s - p.gun;
							points[p2.num] += point;

							guns[x][y].offer(p.gun);
							p.gun = 0;

							int x2 = p.x;
							int y2 = p.y;
							int d2 = p.d;

							int nx2 = x2 + deltas[d2][0];
							int ny2 = y2 + deltas[d2][1];

							while (0 >= nx2 || nx2 > n || 0 >= ny2 || ny2 > n || playerPos[nx2][ny2] != null) {
								d2 = (d2 + 1) % 4;
								p.d = d2;
								nx2 = x2 + deltas[d2][0];
								ny2 = y2 + deltas[d2][1];
							}

							p.x = nx2;
							p.y = ny2;

							playerPos[nx2][ny2] = p;

							if (!guns[nx2][ny2].isEmpty()) {
								p.gun = guns[nx2][ny2].poll();
							}

							playerPos[x][y] = p2;

							if (!guns[x][y].isEmpty() && p2.gun < guns[x][y].peek()) {
								p2.gun = guns[x][y].poll();
							}
						}
					}
				}
			}
		}

		StringBuilder sb = new StringBuilder();
		for (int i = 1; i <= m; ++i) {
			sb.append(points[i]).append(' ');
		}
		System.out.println(sb);
	}
}