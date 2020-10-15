package testowe;

public class Team {
    Member member;

    public Team(Member member) {
        this.member = member;
    }

    public static void main(String[] args) {
        Member myMember = new Member("Rafatus", -1);
        Team myTeam = new Team(myMember);
        System.out.println(myTeam.member.getName());
        System.out.println(myTeam.member.getLevel());
    }
}

class Member {
    private String name;
    private int level;

    public Member(String name, int level) {
        this.name = name;
        this.level = level;
    }

    public String getName() {
        return this.name;
    }

    public int getLevel() {
        return this.level;
    }
}
