package io.javabrains;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class BetterReadsApp {

	public static void main(String[] args) {
		SpringApplication.run(BetterReadsApp.class, args);
	}

	@RequestMapping("/user") //For Marking Book has read ? WHo is marking the book has read
	public String user(@AuthenticationPrincipal OAuth2User principal) {
		System.out.println(principal); //Authenticated person ==>Authentication is established
		return principal.getAttribute("name");

		//Name: [41510667], Granted Authorities: [[ROLE_USER, SCOPE_read:user]],
		// User Attributes: [{login=Asingh1248, id=41510667, node_id=MDQ6VXNlcjQxNTEwNjY3,
		// avatar_url=https://avatars.githubusercontent.com/u/41510667?v=4,
		// gravatar_id=, url=https://api.github.com/users/Asingh1248,
		// html_url=https://github.com/Asingh1248,
		// followers_url=https://api.github.com/users/Asingh1248/followers,
		// following_url=https://api.github.com/users/Asingh1248/following{/other_user},
		// gists_url=https://api.github.com/users/Asingh1248/gists{/gist_id},
		// starred_url=https://api.github.com/users/Asingh1248/starred{/owner}{/repo},
		// subscriptions_url=https://api.github.com/users/Asingh1248/subscriptions,
		// organizations_url=https://api.github.com/users/Asingh1248/orgs,
		// repos_url=https://api.github.com/users/Asingh1248/repos, events_url=https://api.github.com/users/Asingh1248/events{/privacy}, received_events_url=https://api.github.com/users/Asingh1248/received_events, type=User, site_admin=false, name=Animesh Singh, company=null, blog=, location=Navi Mumbai Maharashtra, email=null, hireable=true, bio=I am a Student doing  my Post-Graduation MCA course from KJ Somaiya Institute of Management Studies and I love to Code, twitter_username=null, public_repos=51, public_gists=0,
		// followers=8, following=25,
		// created_at=2018-07-21T11:56:51Z, updated_at=2022-05-13T05:01:49Z}]
	}
	

}
