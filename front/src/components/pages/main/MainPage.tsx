import { useNavigate } from "react-router-dom";
import { Page_Url } from "../../../router/Page_Url";
import styles from "./MainPage.module.scss";
import LanguageSwitcher from "../../molecules/language/LanguageSwitcher";
import lemon from "../../../assets/characterSample/test_dab.gif";

function MainPage() {
  const navigate = useNavigate();

  return (
    <div className={styles.mainContainer}>
      <LanguageSwitcher />
      <h1>Letter Monster</h1>
      <div className={styles.characterDiv}>
        <img className={styles.character} src={lemon} alt="lettermon" />
      </div>
      <button onClick={() => navigate(Page_Url.Login)}>카카오로그인</button>
      <button onClick={() => navigate(Page_Url.Sketch)}>캐릭터그리기</button>
    </div>
  );
}

export default MainPage;